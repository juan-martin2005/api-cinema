package com.jg.sistema.payments.infrastructure;

import com.jg.sistema.payments.application.port.PaymentGateway;
import com.stripe.model.checkout.Session;
import com.stripe.model.Refund;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.RefundCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;
import static org.springframework.http.HttpStatus.*;

@Component @RequiredArgsConstructor
public class StripeGateway implements PaymentGateway {
    private final ObjectMapper json;
    @Value("${stripe.enabled:false}") private boolean enabled;
    @Value("${stripe.allow-live:false}") private boolean allowLive;
    @Value("${stripe.secret-key:}") private String key;
    @Value("${stripe.webhook-secret:}") private String webhookSecret;
    @Value("${stripe.success-url}") private String successUrl;
    @Value("${stripe.cancel-url}") private String cancelUrl;

    public void ensureConfigured() { options(null); }

    private RequestOptions options(String idempotency) {
        if (!enabled || key.isBlank() || (!allowLive && !key.startsWith("sk_test_") && !key.startsWith("rk_test_"))) throw new ResponseStatusException(SERVICE_UNAVAILABLE,"Stripe sandbox is not configured");
        return RequestOptions.builder().setApiKey(key).setIdempotencyKey(idempotency)
                .setConnectTimeout(5000).setReadTimeout(10000).build();
    }
    public Checkout create(UUID attempt, UUID booking, BigDecimal total, Instant expiresAt) {
        var product=SessionCreateParams.LineItem.PriceData.ProductData.builder().setName("Cinema reservation " + booking).build();
        var price=SessionCreateParams.LineItem.PriceData.builder().setCurrency("pen")
                .setUnitAmount(total.movePointRight(2).longValueExact()).setProductData(product).build();
        var params=SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl).setCancelUrl(cancelUrl)
                .setExpiresAt(expiresAt.getEpochSecond())
                .addAllowedPaymentMethodType(SessionCreateParams.AllowedPaymentMethodType.CARD)
                .putMetadata("bookingId",booking.toString()).putMetadata("attemptId",attempt.toString())
                .setClientReferenceId(booking.toString())
                .addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L).setPriceData(price).build()).build();
        try { return map(Session.create(params,options("checkout:"+attempt))); }
        catch (com.stripe.exception.InvalidRequestException ex) {
            // A successful previous request may have lost its response. Recover it before abandoning inventory.
            try {
                var list=Session.list(com.stripe.param.checkout.SessionListParams.builder()
                        .setCreated(com.stripe.param.checkout.SessionListParams.Created.builder()
                                .setGte(expiresAt.minusSeconds(1860).getEpochSecond()).build()).setLimit(100L).build(),options(null));
                for(var candidate:list.autoPagingIterable())
                    if(attempt.toString().equals(candidate.getMetadata().get("attemptId"))) return map(candidate);
            } catch(com.stripe.exception.StripeException lookupFailure) { throw unavailable(lookupFailure); }
            throw new CreationRejected();
        }
        catch (com.stripe.exception.StripeException ex) { throw unavailable(ex); }
    }
    public Checkout retrieve(String id) {
        try { return map(Session.retrieve(id,options(null))); }
        catch (com.stripe.exception.StripeException ex) { throw unavailable(ex); }
    }
    public Checkout expire(String id) {
        try { return map(Session.retrieve(id,options(null)).expire(options("expire:"+id))); }
        catch (com.stripe.exception.InvalidRequestException ex) { return retrieve(id); }
        catch (com.stripe.exception.StripeException ex) { throw unavailable(ex); }
    }
    public RefundResult refund(String idempotencyKey,String intent,String existingRefund) {
        try {
            var refund=existingRefund==null
                    ?Refund.create(RefundCreateParams.builder().setPaymentIntent(intent).build(),options(idempotencyKey))
                    :Refund.retrieve(existingRefund,options(null));
            return new RefundResult(refund.getId(),refund.getStatus());
        } catch (com.stripe.exception.StripeException ex) { throw unavailable(ex); }
    }
    public Notification verify(String body,String signature) {
        if (!enabled || webhookSecret.isBlank()) throw new ResponseStatusException(SERVICE_UNAVAILABLE,"Stripe webhook is not configured");
        try {
            var event=Webhook.constructEvent(body,signature,webhookSecret);
            String sessionId=null;
            if(event.getType().startsWith("checkout.session."))
                sessionId=json.readTree(body).path("data").path("object").path("id").asText();
            return new Notification(event.getId(),event.getType(),sessionId);
        } catch(Exception ex) { throw new ResponseStatusException(BAD_REQUEST,"Invalid Stripe signature or event"); }
    }
    private Checkout map(Session s) {
        return new Checkout(s.getId(),s.getUrl(),s.getPaymentIntent(),s.getStatus(),"paid".equals(s.getPaymentStatus()),
                s.getAmountTotal()==null?0:s.getAmountTotal(),s.getCurrency(),Instant.ofEpochSecond(s.getExpiresAt()),
                s.getMetadata().get("bookingId"),s.getMetadata().get("attemptId"));
    }
    private ResponseStatusException unavailable(Exception ex) {
        return new ResponseStatusException(BAD_GATEWAY,"Stripe request failed; the payment will be reconciled before releasing seats",ex);
    }
}
