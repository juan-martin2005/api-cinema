package com.jg.sistema.booking.infrastructure.persistence;

import com.jg.sistema.booking.application.port.out.BookingDetailRepositoryPort;
import com.jg.sistema.booking.domain.model.BookingDetail;
import com.jg.sistema.booking.infrastructure.persistence.mapper.BookingDetailMapper;
import com.jg.sistema.booking.infrastructure.persistence.repositories.PostgreSqlBookingDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaBookingDetailRepository implements BookingDetailRepositoryPort {

    private final PostgreSqlBookingDetailRepository postgreSqlBookingDetailRepository;

    @Override
    public List<BookingDetail> findByBookingId(UUID bookingId) {
        return postgreSqlBookingDetailRepository.findByBookingId(bookingId).stream()
                .map(BookingDetailMapper::toDomain)
                .collect(Collectors.toList());
    }
}
