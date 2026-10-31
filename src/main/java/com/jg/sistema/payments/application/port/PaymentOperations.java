package com.jg.sistema.payments.application.port;
import java.util.UUID;
import com.jg.sistema.booking.domain.model.Booking;
public interface PaymentOperations {
    Booking cancel(UUID bookingId);
    Booking reconcile(UUID bookingId);
}
