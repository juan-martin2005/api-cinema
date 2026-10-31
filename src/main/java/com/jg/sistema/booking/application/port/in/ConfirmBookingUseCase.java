package com.jg.sistema.booking.application.port.in;

import com.jg.sistema.booking.domain.model.Booking;

import java.util.UUID;

public interface ConfirmBookingUseCase {
    Booking confirmBooking(UUID id);
}
