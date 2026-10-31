package com.jg.sistema.booking.infrastructure.persistence.repositories;

import com.jg.sistema.booking.infrastructure.persistence.entities.BookingDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostgreSqlBookingDetailRepository extends JpaRepository<BookingDetailEntity, UUID> {
    List<BookingDetailEntity> findByBookingId(UUID bookingId);
}
