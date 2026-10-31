package com.jg.sistema.booking.infrastructure.persistence.repositories;

import com.jg.sistema.booking.infrastructure.persistence.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostgreSqlBookingRepository extends JpaRepository<BookingEntity, UUID> {
    Optional<BookingEntity> findByCode(String code);
    List<BookingEntity> findByUserIdOrderByCreatAtDesc(UUID userId);
    List<BookingEntity> findByShowTimeEntityId(UUID showTimeId);
    boolean existsByCode(String code);

    @Query(value = "SELECT CONCAT(s.\"row\",s.number) FROM seat_occupations o JOIN seats s ON s.id=o.seat_id WHERE o.show_time_id=:showTimeId", nativeQuery = true)
    List<String> findOccupiedSeatLabels(@Param("showTimeId") UUID showTimeId, @Param("now") LocalDateTime now);
}
