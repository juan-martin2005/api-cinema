package com.jg.sistema;

import com.jg.sistema.booking.application.service.BookingService;
import com.jg.sistema.booking.domain.model.*;
import com.jg.sistema.payments.application.PaymentService;
import com.jg.sistema.payments.application.port.PaymentGateway;
import com.jg.sistema.show_time.application.service.ShowTimeService;
import com.jg.sistema.show_time.domain.model.ShowTime;
import com.jg.sistema.movie.domain.model.Movie;
import com.jg.sistema.cinema_room.domain.model.CinemaRoom;
import com.jg.sistema.cinema.domain.model.Format;
import com.jg.sistema.user.application.service.UserService;
import com.jg.sistema.auth.infrastructure.security.JwtService;
import com.jg.sistema.auth.infrastructure.config.JwtAuthenticationFilter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.mock.web.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest @ActiveProfiles("test")
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
class BookingPaymentTests {
    @Autowired org.springframework.test.web.servlet.MockMvc mvc;
    @Autowired JdbcTemplate jdbc;
    @Autowired BookingService bookings;
    @Autowired PaymentService payments;
    @Autowired ShowTimeService showtimes;
    @Autowired UserService users;
    @Autowired UserDetailsService details;
    @Autowired JwtService jwt;
    @Autowired JwtAuthenticationFilter filter;
    @Autowired com.jg.sistema.cinema_room.application.service.CinemaRoomService rooms;
    @Autowired com.jg.sistema.movie.application.service.MovieService movies;
    @Autowired com.jg.sistema.auth.application.service.RefreshTokenService refresh;
    @Autowired com.jg.sistema.auth.application.service.OAuthAccountService oauth;
    @Autowired com.jg.sistema.auth.application.service.EmailService emailService;
    @MockitoBean PaymentGateway gateway;
    @MockitoBean Clock clock;
    UUID user,room,movie,show,seat,cinema;
    AtomicReference<PaymentGateway.Checkout> session=new AtomicReference<>();

    @BeforeEach void setup() {
        when(clock.instant()).thenReturn(Instant.parse("2026-11-01T17:00:00Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("America/Lima"));
        for(String table:List.of("payment_events","payment_attempts","seat_occupations","booking_details","bookings","show_times","seats","cinema_rooms","cinema_formats","cinemas","movies","oauth_codes","oauth_identities","refresh_token","email_verifications","mail_outbox","auth_limits","user_roles","users")) jdbc.update("DELETE FROM "+table);
        jdbc.update("DELETE FROM formats");
        user=UUID.randomUUID();room=UUID.randomUUID();movie=UUID.randomUUID();show=UUID.randomUUID();seat=UUID.randomUUID();cinema=UUID.randomUUID();
        jdbc.update("INSERT INTO users(id,email,is_active,is_verified) VALUES (?,'owner@example.test',TRUE,TRUE)",user);
        jdbc.update("INSERT INTO user_roles(user_id,role_id) SELECT ?,id FROM roles WHERE name='ADMIN'",user);
        jdbc.update("INSERT INTO formats(id,name,price) VALUES (100,'2D',18)");
        jdbc.update("INSERT INTO cinemas(id,name,address,city) VALUES (?,'Cinema','Address','Lima')",cinema);
        jdbc.update("INSERT INTO cinema_formats(cinema_id,format_id) VALUES (?,100)",cinema);
        jdbc.update("INSERT INTO cinema_rooms(id,name,capacity,cinema_id) VALUES (?,'Room',1,?)",room,cinema);
        jdbc.update("INSERT INTO seats(id,\"row\",number,pos_x,pos_y,room_id) VALUES (?,'A',1,0,0,?)",seat,room);
        jdbc.update("INSERT INTO movies(id,title,duration) VALUES (?,'Film',120)",movie);
        jdbc.update("INSERT INTO show_times(id,movie_id,cinema_room_id,format_id,start_time,end_time) VALUES (?,?,?,100,?,?)",show,movie,room,LocalDateTime.now(clock).plusHours(2),LocalDateTime.now(clock).plusHours(4));
        signIn();
        when(gateway.create(any(),any(),any(),any())).thenAnswer(inv -> {
            var s=new PaymentGateway.Checkout("cs_test_"+inv.getArgument(0),"https://checkout.stripe.com/test","pi_test","open",false,
                    ((BigDecimal)inv.getArgument(2)).movePointRight(2).longValueExact(),"pen",inv.getArgument(3),inv.getArgument(1).toString(),inv.getArgument(0).toString());
            session.set(s);return s;
        });
        when(gateway.retrieve(anyString())).thenAnswer(inv -> session.get());
    }
    void signIn() {
        var principal=details.loadUserByUsername("owner@example.test");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal,null,principal.getAuthorities()));
    }
    @AfterEach void clear() { SecurityContextHolder.clearContext(); }
    Booking reserve() { return bookings.createBooking(show,List.of(seat)); }
    PaymentGateway.Checkout paid() {
        var s=session.get();return new PaymentGateway.Checkout(s.id(),s.url(),s.paymentIntent(),"complete",true,s.amount(),s.currency(),s.expiresAt(),s.bookingId(),s.attemptId());
    }
    @Test void reservationIsPendingAndPricedOnServer() {
        var b=reserve();assertEquals(BookingStatus.PENDING,b.bookingStatus());assertEquals(PaymentStatus.UNPAID,b.paymentStatus());
        assertEquals(new BigDecimal("21.24"),b.total());assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM seat_occupations",Integer.class));
        assertThrows(RuntimeException.class,this::reserve);
    }
    @Test void otherUserCannotReadOrCancelReservation() {
        var b=reserve();var id=UUID.randomUUID();
        jdbc.update("INSERT INTO users(id,email,is_active,is_verified) VALUES (?,'other@example.test',TRUE,TRUE)",id);
        var principal=details.loadUserByUsername("other@example.test");SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal,null,principal.getAuthorities()));
        assertEquals(403,assertThrows(ResponseStatusException.class,()->bookings.getBookingById(b.id())).getStatusCode().value());
        assertThrows(ResponseStatusException.class,()->payments.cancel(b.id()));
    }
    @Test void concurrentReservationHasExactlyOneWinner() throws Exception {
        var barrier=new CyclicBarrier(2);
        try(var executor=Executors.newFixedThreadPool(2)) {
            Callable<Boolean> task=()-> {signIn();barrier.await();try {reserve();return true;}catch(RuntimeException e){return false;}finally{clear();}};
            var a=executor.submit(task);var b=executor.submit(task);
            assertNotEquals(a.get(15,TimeUnit.SECONDS),b.get(15,TimeUnit.SECONDS));
        }
        assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM bookings",Integer.class));
    }
    @Test void concurrentOverlappingShowtimesHaveExactlyOneWinner() throws Exception {
        jdbc.update("DELETE FROM show_times");var barrier=new CyclicBarrier(2);
        try(var executor=Executors.newFixedThreadPool(2)) {
            Callable<Boolean> task=()-> {barrier.await();try {
                showtimes.createShowTime(new ShowTime(null,Movie.withId(movie),CinemaRoom.withId(room),LocalDateTime.now(clock).plusHours(2),null,new Format(100L,null,null)));return true;
            }catch(RuntimeException e){return false;}};
            var a=executor.submit(task);var b=executor.submit(task);assertNotEquals(a.get(15,TimeUnit.SECONDS),b.get(15,TimeUnit.SECONDS));
        }
        assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM show_times",Integer.class));
    }
    @Test void repeatedWebhookConfirmsOnceAndChecksAmount() {
        var b=reserve();payments.checkout(b.id());session.set(paid());
        when(gateway.verify("event","signature")).thenReturn(new PaymentGateway.Notification("evt_1","checkout.session.completed",session.get().id()));
        payments.webhook("event","signature");payments.webhook("event","signature");
        assertEquals(BookingStatus.CONFIRMED,bookings.getBookingById(b.id()).bookingStatus());
        assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM payment_events",Integer.class));
    }
    @Test void unpaidPaymentCannotBeConfirmed() {
        var b=reserve();payments.checkout(b.id());assertThrows(ResponseStatusException.class,()->payments.reconcile(b.id()));
        assertEquals(BookingStatus.PENDING,bookings.getBookingById(b.id()).bookingStatus());
    }
    @Test void refundIsNotMarkedCompleteUntilStripeSucceeds() {
        var b=reserve();payments.checkout(b.id());session.set(paid());payments.reconcile(b.id());
        when(gateway.refund(anyString(),anyString(),nullable(String.class))).thenReturn(new PaymentGateway.RefundResult("re_1","pending"));
        assertEquals(PaymentStatus.REFUND_PENDING,payments.cancel(b.id()).paymentStatus());
        assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM seat_occupations",Integer.class));
        when(gateway.refund(anyString(),anyString(),eq("re_1"))).thenReturn(new PaymentGateway.RefundResult("re_1","succeeded"));
        assertEquals(PaymentStatus.REFUNDED,payments.cancel(b.id()).paymentStatus());
        assertEquals(0,jdbc.queryForObject("SELECT COUNT(*) FROM seat_occupations",Integer.class));
    }
    @Test void invalidAmountCannotConfirm() {
        var b=reserve();payments.checkout(b.id());var s=paid();session.set(new PaymentGateway.Checkout(s.id(),s.url(),s.paymentIntent(),"complete",true,1,"pen",s.expiresAt(),s.bookingId(),s.attemptId()));
        assertThrows(ResponseStatusException.class,()->payments.reconcile(b.id()));
        assertEquals(BookingStatus.PENDING,bookings.getBookingById(b.id()).bookingStatus());
    }
    @Test void jwtRejectsDisabledAccountAndRetiredPermissions() throws Exception {
        String token=jwt.generateToken("owner@example.test");users.toggleUserStatus(user);clear();
        var req=new MockHttpServletRequest();req.addHeader("Authorization","Bearer "+token);
        filter.doFilter(req,new MockHttpServletResponse(),new MockFilterChain());assertNull(SecurityContextHolder.getContext().getAuthentication());
        jdbc.update("UPDATE users SET is_active=TRUE WHERE id=?",user);
        filter.doFilter(newRequest(token),new MockHttpServletResponse(),new MockFilterChain());assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    MockHttpServletRequest newRequest(String token) {var r=new MockHttpServletRequest();r.addHeader("Authorization","Bearer "+token);return r;}

    @Test void persistedCoordinatesAndCapacityMatchRequest() {
        rooms.createCinemaRoom(new CinemaRoom(null,"New room",1,List.of(),new com.jg.sistema.cinema.domain.model.Cinema(cinema,null,null,null,null,null,null,null,null,null,null)),
                List.of(new com.jg.sistema.cinema_room.domain.model.Seat(null,2,"B",4,6)));
        var result=jdbc.queryForMap("SELECT s.pos_x,s.pos_y,r.capacity FROM seats s JOIN cinema_rooms r ON s.room_id=r.id WHERE r.name='New room'");
        assertEquals(4,((Number)result.get("pos_x")).intValue());assertEquals(6,((Number)result.get("pos_y")).intValue());assertEquals(1,((Number)result.get("capacity")).intValue());
    }
    @Test void movieUpdateInvalidatesCachedValue() {
        var original=movies.getById(movie);
        movies.updateMovie(new Movie(movie,"Updated film","Description",null,"PG",120,"Drama"),movie,null);
        assertEquals("Updated film",movies.getById(movie).title());
        assertThrows(ResponseStatusException.class,()->movies.updateMovie(new Movie(movie,"Updated film","Description",null,"PG",80,"Drama"),movie,null));
    }
    @Test void rotatedRefreshCannotBeReused() {
        String token=refresh.createRefreshToken(user);
        var pair=refresh.refreshAccessToken(token);assertNotEquals(token,pair.refreshToken());
        assertThrows(com.jg.sistema.auth.domain.exception.RefreshTokenNotFoundException.class,()->refresh.refreshAccessToken(token));
    }
    @Test void expiredRefreshRevocationCommitsEvenOnError() {
        String token=refresh.createRefreshToken(user);
        jdbc.update("UPDATE refresh_token SET expiry_date=?",java.time.OffsetDateTime.now().minusDays(1));
        assertThrows(com.jg.sistema.auth.domain.exception.RefreshTokenExpiredException.class,()->refresh.refreshAccessToken(token));
        assertEquals(0,jdbc.queryForObject("SELECT COUNT(*) FROM refresh_token",Integer.class));
    }
    @Test void oauthRequiresExplicitLinkAndConsumesCodeOnce() {
        String code=oauth.issue("google","provider-subject","owner@example.test","Owner","User");
        assertThrows(ResponseStatusException.class,()->oauth.exchange(code,false));
        assertNotNull(oauth.exchange(code,true).accessToken());
        assertThrows(ResponseStatusException.class,()->oauth.exchange(code,true));
        assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM oauth_identities",Integer.class));
    }
    @Test void timeoutRetriesUseSameAttemptAndExpiry() {
        var b=reserve();doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.BAD_GATEWAY)).when(gateway).create(any(),any(),any(),any());
        assertThrows(ResponseStatusException.class,()->payments.checkout(b.id()));
        assertThrows(ResponseStatusException.class,()->payments.checkout(b.id()));
        var attempts=org.mockito.ArgumentCaptor.forClass(UUID.class);var expiries=org.mockito.ArgumentCaptor.forClass(Instant.class);
        verify(gateway,times(2)).create(attempts.capture(),eq(b.id()),any(),expiries.capture());
        assertEquals(attempts.getAllValues().getFirst(),attempts.getAllValues().getLast());assertEquals(expiries.getAllValues().getFirst(),expiries.getAllValues().getLast());
        assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM seat_occupations",Integer.class));
    }
    @Test void paidLateAfterCancellationQueuesRefundWithoutOccupyingAgain() {
        var b=reserve();payments.checkout(b.id());var open=session.get();
        var expired=new PaymentGateway.Checkout(open.id(),open.url(),null,"expired",false,open.amount(),open.currency(),open.expiresAt(),open.bookingId(),open.attemptId());
        when(gateway.expire(anyString())).thenReturn(expired);payments.cancel(b.id());
        assertEquals(0,jdbc.queryForObject("SELECT COUNT(*) FROM seat_occupations",Integer.class));
        session.set(paid());when(gateway.verify("late","sig")).thenReturn(new PaymentGateway.Notification("evt_late","checkout.session.completed",open.id()));
        payments.webhook("late","sig");var late=bookings.getBookingById(b.id());assertEquals(BookingStatus.CANCELLED,late.bookingStatus());assertEquals(PaymentStatus.REFUND_PENDING,late.paymentStatus());
        assertEquals(0,jdbc.queryForObject("SELECT COUNT(*) FROM seat_occupations",Integer.class));
    }
    @Test void expiredReservationWithoutCheckoutReleasesSeats() {
        var b=reserve();jdbc.update("UPDATE bookings SET expired_at=? WHERE id=?",LocalDateTime.now(clock).minusMinutes(1),b.id());
        org.springframework.test.util.ReflectionTestUtils.setField(payments,"jobsEnabled",true);
        try { payments.reconcilePending(); } finally { org.springframework.test.util.ReflectionTestUtils.setField(payments,"jobsEnabled",false); }
        assertEquals(BookingStatus.EXPIRED,bookings.getBookingById(b.id()).bookingStatus());assertEquals(0,jdbc.queryForObject("SELECT COUNT(*) FROM seat_occupations",Integer.class));
    }
    @Test void checkoutCannotExtendHoldPastShowtime() {
        var b=reserve();jdbc.update("UPDATE show_times SET start_time=? WHERE id=?",LocalDateTime.now(clock).plusMinutes(20),show);
        assertEquals(409,assertThrows(ResponseStatusException.class,()->payments.checkout(b.id())).getStatusCode().value());
        assertEquals(0,jdbc.queryForObject("SELECT COUNT(*) FROM payment_attempts",Integer.class));
    }
    @Test void otpAttemptsAreLimitedAndMailIsDurable() {
        jdbc.update("UPDATE users SET is_active=FALSE,is_verified=FALSE WHERE id=?",user);
        emailService.sendCodeToEmail("OWNER@example.test");
        assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM mail_outbox WHERE sent=FALSE",Integer.class));
        for(int i=0;i<5;i++) assertThrows(com.jg.sistema.auth.domain.exception.VerifyCodeException.class,()->emailService.verifyEmail("owner@example.test","bad"));
        assertEquals(429,assertThrows(ResponseStatusException.class,()->emailService.verifyEmail("owner@example.test","bad")).getStatusCode().value());
        assertEquals(429,assertThrows(ResponseStatusException.class,()->emailService.resendEmailVerificationCode("owner@example.test")).getStatusCode().value());
    }
    @Test void httpCatalogUsesPaginationAndRoomRenameRejectsIgnoredFields() throws Exception {
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/movies").param("size","1").param("sort","title,desc"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.content[0].title").value("Film"));
        String token=jwt.generateToken("owner@example.test");
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/cinema-rooms/"+room)
                .header("Authorization","Bearer "+token).contentType("application/json").content("{\"name\":\"Changed\",\"seats\":[]}"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isBadRequest());
    }
}
