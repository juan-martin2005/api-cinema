package com.jg.sistema.booking.application.port.out;

import com.jg.sistema.booking.domain.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepositoryPort {
    Page<Booking> findPage(Pageable pageable);
    Booking save(Booking booking);
    Optional<Booking> findById(UUID id);
    Optional<Booking> findByCode(String code);
    List<Booking> findAll();
    List<Booking> findByUserId(UUID userId);
    List<Booking> findByShowTimeId(UUID showTimeId);
    List<String> findOccupiedSeatLabelsByShowTimeId(UUID showTimeId, LocalDateTime now);
    boolean existsByCode(String code);
}
