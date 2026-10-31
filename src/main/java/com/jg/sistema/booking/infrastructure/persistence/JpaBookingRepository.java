package com.jg.sistema.booking.infrastructure.persistence;

import com.jg.sistema.booking.application.port.out.BookingRepositoryPort;
import com.jg.sistema.booking.domain.model.Booking;
import com.jg.sistema.booking.infrastructure.persistence.entities.BookingEntity;
import com.jg.sistema.booking.infrastructure.persistence.mapper.BookingMapper;
import com.jg.sistema.booking.infrastructure.persistence.repositories.PostgreSqlBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaBookingRepository implements BookingRepositoryPort {

    private final PostgreSqlBookingRepository postgreSqlBookingRepository;

    @Override
    public Booking save(Booking booking) {
        BookingEntity entity = BookingMapper.toEntity(booking);
        BookingEntity saved = postgreSqlBookingRepository.save(entity);
        return BookingMapper.toDomain(saved);
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return postgreSqlBookingRepository.findById(id)
                .map(BookingMapper::toDomain);
    }

    @Override
    public Optional<Booking> findByCode(String code) {
        return postgreSqlBookingRepository.findByCode(code)
                .map(BookingMapper::toDomain);
    }

    @Override
    public List<Booking> findAll() {
        return postgreSqlBookingRepository.findAll().stream()
                .map(BookingMapper::toDomain)
                .collect(Collectors.toList());
    }



    @Override
    public List<Booking> findByUserId(UUID userId) {
        return postgreSqlBookingRepository.findByUserIdOrderByCreatAtDesc(userId).stream()
                .map(BookingMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByShowTimeId(UUID showTimeId) {
        return postgreSqlBookingRepository.findByShowTimeEntityId(showTimeId).stream()
                .map(BookingMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findOccupiedSeatLabelsByShowTimeId(UUID showTimeId, LocalDateTime now) {
        return postgreSqlBookingRepository.findOccupiedSeatLabels(showTimeId, now);
    }

    @Override
    public boolean existsByCode(String code) {
        return postgreSqlBookingRepository.existsByCode(code);
    }
}
