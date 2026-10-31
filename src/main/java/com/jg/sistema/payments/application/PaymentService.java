package com.jg.sistema.payments.application;

import com.jg.sistema.payments.application.port.*;
import com.jg.sistema.booking.application.port.out.BookingRepositoryPort;
import com.jg.sistema.booking.domain.model.Booking;
import com.jg.sistema.user.application.port.out.UserContextHolderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Value;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import static org.springframework.http.HttpStatus.*;

/** External calls are outside transactions; durable attempts are retried with the same idempotency key. */
@Service @RequiredArgsConstructor @Slf4j
public class PaymentService implements PaymentOperations {
    private final JdbcTemplate jdbc;
    private final PlatformTransactionManager manager;
    private final PaymentGateway gateway;
    private final BookingRepositoryPort bookings;
    private final UserContextHolderPort current;
    private final Clock clock;
    @Value("${app.jobs-enabled:true}") private boolean jobsEnabled;
    public record CheckoutResponse(String sessionId,String url,LocalDateTime expiresAt) {}
    private record Attempt(UUID id,UUID booking,String session,String state,LocalDateTime expires,String refund,int generation) {}
    private TransactionTemplate tx() { return new TransactionTemplate(manager); }
    private LocalDateTime now() { return LocalDateTime.now(clock); }
    private Map<String,Object> lock(UUID id) {
        var rows=jdbc.queryForList("SELECT * FROM bookings WHERE id=? FOR UPDATE",id);
        if(rows.isEmpty()) throw new ResponseStatusException(NOT_FOUND,"Reservation not found");
        return rows.getFirst();
    }
    private void authorize(Map<String,Object> b) {
        if(current.getCurrentUserId()==null || (!current.isAdmin() && !current.getCurrentUserId().equals(b.get("user_id"))))
            throw new ResponseStatusException(FORBIDDEN,"Reservation belongs to another user");
    }
    private Attempt attempt(UUID booking) {
        var rows=jdbc.queryForList("SELECT * FROM payment_attempts WHERE booking_id=?",booking);
        if(rows.isEmpty()) return null;
        var p=rows.getFirst(); return new Attempt((UUID)p.get("id"),booking,(String)p.get("session_id"),(String)p.get("state"),date(p.get("checkout_expires_at")),(String)p.get("refund_id"),((Number)p.get("refund_generation")).intValue());
    }
    public CheckoutResponse checkout(UUID id) {
        gateway.ensureConfigured();
        Attempt a=tx().execute(status -> {
            var b=lock(id);authorize(b);
            if(!"PENDING".equals(b.get("booking_status")) || !date(b.get("expired_at")).isAfter(now()))
                throw new ResponseStatusException(CONFLICT,"Reservation is not pending or has expired");
            Attempt existing=attempt(id);
            if(existing!=null) return existing;
            UUID aid=UUID.randomUUID();
            var expires=now().plusMinutes(31).withNano(0);
            jdbc.update("INSERT INTO payment_attempts(id,booking_id,state,updated_at,checkout_expires_at) VALUES (?,?,'CREATING',?,?)",aid,id,now(),expires);
            return new Attempt(aid,id,null,"CREATING",expires,null,0);
        });
        var session=a.session()==null?createSession(a):gateway.retrieve(a.session());
        return new CheckoutResponse(session.id(),session.url(),LocalDateTime.ofInstant(session.expiresAt(),clock.getZone()));
    }
    private PaymentGateway.Checkout createSession(Attempt a) {
        BigDecimal total=jdbc.queryForObject("SELECT total FROM bookings WHERE id=?",BigDecimal.class,a.booking());
        PaymentGateway.Checkout session;
        try { session=gateway.create(a.id(),a.booking(),total,a.expires().atZone(clock.getZone()).toInstant()); }
        catch (PaymentGateway.CreationRejected ex) {
            tx().executeWithoutResult(status -> {
                var b=lock(a.booking());
                jdbc.update("UPDATE payment_attempts SET state='REJECTED',updated_at=? WHERE id=?",now(),a.id());
                if("PENDING".equals(b.get("booking_status"))) jdbc.update("UPDATE bookings SET booking_status='EXPIRED',payment_status='FAILED' WHERE id=?",a.booking());
                else jdbc.update("UPDATE bookings SET payment_status='UNPAID' WHERE id=? AND payment_status='CANCEL_PENDING'",a.booking());
                release(a.booking());
            });
            throw new ResponseStatusException(BAD_GATEWAY,"Checkout could not be created; reservation released",ex);
        }
        tx().executeWithoutResult(status -> {
            var b=lock(a.booking()); verify(session,a,b);
            jdbc.update("UPDATE payment_attempts SET session_id=?,checkout_url=?,state='READY',updated_at=?,last_error=NULL WHERE id=?",session.id(),session.url(),now(),a.id());
            if("PENDING".equals(b.get("booking_status")))
                jdbc.update("UPDATE bookings SET expired_at=? WHERE id=?",LocalDateTime.ofInstant(session.expiresAt(),clock.getZone()),a.booking());
        });
        return session;
    }
    public Booking reconcile(UUID id) {
        tx().executeWithoutResult(status -> authorize(lock(id)));
        recover(id,false);
        Booking result=bookings.findById(id).orElseThrow();
        if(result.bookingStatus()!=com.jg.sistema.booking.domain.model.BookingStatus.CONFIRMED)
            throw new ResponseStatusException(CONFLICT,"Stripe has not confirmed a payment for this reservation");
        return result;
    }
    public Booking cancel(UUID id) {
        tx().executeWithoutResult(status -> {
            var b=lock(id);authorize(b);
            if("USED".equals(b.get("booking_status"))) throw new ResponseStatusException(CONFLICT,"Used reservation cannot be cancelled");
            LocalDateTime start=jdbc.queryForObject("SELECT start_time FROM show_times WHERE id=?",LocalDateTime.class,b.get("show_time_entity_id"));
            if(!start.isAfter(now())) throw new ResponseStatusException(CONFLICT,"Showtime has already started");
            if(!List.of("PENDING","CONFIRMED","CANCELLED").contains(b.get("booking_status"))) throw new ResponseStatusException(CONFLICT,"Reservation cannot be cancelled");
            if(!"CANCELLED".equals(b.get("booking_status")))
                jdbc.update("UPDATE bookings SET booking_status='CANCELLED',payment_status=? WHERE id=?","PAID".equals(b.get("payment_status"))?"REFUND_PENDING":"CANCEL_PENDING",id);
            if(attempt(id)==null) { release(id); jdbc.update("UPDATE bookings SET payment_status='UNPAID' WHERE id=?",id); }
        });
        try { recover(id,false); }
        catch (ResponseStatusException ex) { log.warn("Cancellation queued for reconciliation: {}",id); }
        return bookings.findById(id).orElseThrow();
    }
    public void webhook(String body,String signature) {
        var event=gateway.verify(body,signature);
        if(event.sessionId()==null || event.sessionId().isBlank()) return;
        var session=gateway.retrieve(event.sessionId());
        UUID id;
        try { id=UUID.fromString(session.bookingId()); }
        catch(Exception ex) { throw new ResponseStatusException(BAD_REQUEST,"Missing reservation metadata"); }
        tx().executeWithoutResult(status -> {
            var b=lock(id);
            if(jdbc.queryForObject("SELECT COUNT(*) FROM payment_events WHERE event_id=?",Long.class,event.id())>0) return;
            var a=attempt(id); if(a==null) throw new ResponseStatusException(CONFLICT,"Payment attempt is not yet available");
            apply(session,a,b);
            jdbc.update("INSERT INTO payment_events(event_id,processed_at) VALUES (?,?)",event.id(),now());
        });
        // Durable REFUND_PENDING is processed by the reconciler even if this process stops here.
    }
    private void verify(PaymentGateway.Checkout s,Attempt a,Map<String,Object> b) {
        if(!a.id().toString().equals(s.attemptId()) || !a.booking().toString().equals(s.bookingId())
                || !"pen".equalsIgnoreCase(s.currency()) || ((BigDecimal)b.get("total")).movePointRight(2).longValueExact()!=s.amount()
                || (a.session()!=null && !a.session().equals(s.id())))
            throw new ResponseStatusException(CONFLICT,"Stripe payment does not match the reservation");
    }
    private void apply(PaymentGateway.Checkout s,Attempt a,Map<String,Object> b) {
        verify(s,a,b);
        jdbc.update("UPDATE payment_attempts SET session_id=?,payment_intent_id=?,updated_at=? WHERE id=?",s.id(),s.paymentIntent(),now(),a.id());
        if(s.paid()) {
            if("REFUNDED".equals(b.get("payment_status"))) return;
            if(List.of("CANCELLED","EXPIRED","REFUSED").contains(b.get("booking_status"))) {
                jdbc.update("UPDATE bookings SET payment_status='REFUND_PENDING' WHERE id=?",a.booking());
            } else if("PENDING".equals(b.get("booking_status"))) {
                Long count=jdbc.queryForObject("SELECT COUNT(*) FROM seat_occupations WHERE booking_id=?",Long.class,a.booking());
                Long details=jdbc.queryForObject("SELECT COUNT(*) FROM booking_details WHERE booking_id=?",Long.class,a.booking());
                if(count.longValue()!=details.longValue() || count==0) {
                    jdbc.update("UPDATE bookings SET booking_status='CANCELLED',payment_status='REFUND_PENDING' WHERE id=?",a.booking());
                } else jdbc.update("UPDATE bookings SET booking_status='CONFIRMED',payment_status='PAID' WHERE id=?",a.booking());
            }
        } else if("expired".equals(s.status())) {
            jdbc.update("UPDATE bookings SET booking_status=CASE WHEN booking_status='PENDING' THEN 'EXPIRED' ELSE booking_status END,payment_status=CASE WHEN payment_status='CANCEL_PENDING' THEN 'UNPAID' ELSE payment_status END WHERE id=?",a.booking());
            release(a.booking());
        }
    }
    private void release(UUID id) { jdbc.update("DELETE FROM seat_occupations WHERE booking_id=?",id); }
    private static LocalDateTime date(Object value) { return value instanceof LocalDateTime t?t:((java.sql.Timestamp)value).toLocalDateTime(); }
    private void recover(UUID id,boolean expire) {
        Attempt a=attempt(id);
        if(a==null) {
            if(expire) tx().executeWithoutResult(status -> {
                var b=lock(id);
                if(attempt(id)==null && "PENDING".equals(b.get("booking_status")) && !date(b.get("expired_at")).isAfter(now())) {
                    jdbc.update("UPDATE bookings SET booking_status='EXPIRED' WHERE id=?",id);release(id);
                }
            });
            return;
        }
        var session=a.session()==null?createSession(a):gateway.retrieve(a.session());
        String bookingStatus=jdbc.queryForObject("SELECT booking_status FROM bookings WHERE id=?",String.class,id);
        if("open".equals(session.status()) && (expire || "CANCELLED".equals(bookingStatus))) session=gateway.expire(session.id());
        final var resolved=session;
        tx().executeWithoutResult(status -> apply(resolved,attempt(id),lock(id)));
        String payment=jdbc.queryForObject("SELECT payment_status FROM bookings WHERE id=?",String.class,id);
        if("REFUND_PENDING".equals(payment) && session.paid()) {
            var refund=gateway.refund("refund:"+a.id()+":"+a.generation(),session.paymentIntent(),a.refund());
            tx().executeWithoutResult(status -> {
                lock(id);
                jdbc.update("UPDATE payment_attempts SET refund_id=?,state=?,updated_at=? WHERE id=?",refund.id(),"succeeded".equals(refund.status())?"REFUNDED":"REFUNDING",now(),a.id());
                if("succeeded".equals(refund.status())) {
                    jdbc.update("UPDATE bookings SET payment_status='REFUNDED' WHERE id=?",id);release(id);
                } else if("failed".equals(refund.status()) || "canceled".equals(refund.status())) {
                    jdbc.update("UPDATE payment_attempts SET refund_id=NULL,refund_generation=refund_generation+1,state='REFUND_RETRY' WHERE id=?",a.id());
                }
            });
        }
    }
    @Scheduled(fixedDelayString="${app.payment-reconcile-delay:30000}")
    public void reconcilePending() {
        if(!jobsEnabled) return;
        var ids=jdbc.queryForList("SELECT b.id FROM bookings b LEFT JOIN payment_attempts p ON p.booking_id=b.id WHERE (b.booking_status='PENDING' AND b.expired_at<=?) OR b.payment_status IN ('CANCEL_PENDING','REFUND_PENDING') OR (p.state='CREATING' AND p.updated_at<=?) ORDER BY b.creat_at LIMIT 100",UUID.class,now(),now().minusSeconds(30));
        for(UUID id:ids) {
            try {
                boolean expired=Boolean.TRUE.equals(jdbc.queryForObject("SELECT expired_at<=? FROM bookings WHERE id=?",Boolean.class,now(),id));
                recover(id,expired);
            } catch(Exception ex) { log.warn("Payment reconciliation will retry reservation {} ({})",id,ex.getClass().getSimpleName()); }
        }
    }
}
