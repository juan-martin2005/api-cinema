package com.jg.sistema.payments.application.port;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface PaymentGateway {
    void ensureConfigured();
    /** A definitive creation rejection after checking that no prior session exists. */
    class CreationRejected extends RuntimeException {
        public CreationRejected() { super("Stripe rejected checkout creation"); }
    }
    record Checkout(String id, String url, String paymentIntent, String status, boolean paid,
                    long amount, String currency, Instant expiresAt, String bookingId, String attemptId) {}
    record RefundResult(String id, String status) {}
    record Notification(String id, String type, String sessionId) {}
    Checkout create(UUID attempt, UUID booking, BigDecimal total, Instant expiresAt);
    Checkout retrieve(String sessionId);
    Checkout expire(String sessionId);
    RefundResult refund(String idempotencyKey, String paymentIntent, String existingRefund);
    Notification verify(String body, String signature);
}
