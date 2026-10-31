package com.jg.sistema.booking.application.port.in;

import com.jg.sistema.booking.domain.model.Booking;

import java.util.List;
import java.util.UUID;

public interface CreateBookingUseCase {
    Booking createBooking(UUID showTimeId, List<UUID> seatIds);
}
