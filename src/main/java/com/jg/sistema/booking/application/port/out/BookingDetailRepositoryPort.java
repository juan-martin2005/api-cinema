package com.jg.sistema.booking.application.port.out;

import com.jg.sistema.booking.domain.model.BookingDetail;

import java.util.List;
import java.util.UUID;

public interface BookingDetailRepositoryPort {
    List<BookingDetail> findByBookingId(UUID bookingId);
}
