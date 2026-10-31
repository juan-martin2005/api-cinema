package com.jg.sistema.payments.infrastructure;
import com.jg.sistema.payments.application.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.UUID;
@RestController @RequiredArgsConstructor
public class PaymentController {
    private final PaymentService payments;
    @PostMapping("/api/bookings/{id}/checkout") @PreAuthorize("isAuthenticated()")
    @io.swagger.v3.oas.annotations.Operation(summary="Create or retrieve Stripe Checkout for a pending reservation",description="Returns HTTP 409 if expired or not pending. Amount and PEN currency are computed by the server.")
    public PaymentService.CheckoutResponse checkout(@PathVariable UUID id) { return payments.checkout(id); }
    @PostMapping("/api/payments/stripe/webhook")
    public void webhook(@RequestBody String body,@RequestHeader("Stripe-Signature") String signature) { payments.webhook(body,signature); }
}
