package com.jg.sistema.booking.application.port.in;

import com.jg.sistema.booking.domain.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GetBookingUseCase {
    Page<Booking> findPage(Pageable pageable);
    List<Booking> getAllBookings();
    List<Booking> getBookings();
    Booking getBookingById(UUID id);
    Booking getBookingByCode(String code);
    List<Booking> getMyBookings();
    List<Booking> getBookingsByUserId(UUID userId);
    List<String> getOccupiedSeatsByShowTime(UUID showTimeId);
}
