package com.jg.sistema.booking.infrastructure.persistence.mapper;

import com.jg.sistema.booking.domain.model.BookingDetail;
import com.jg.sistema.booking.infrastructure.controller.dto.BookingDetailResponseDto;
import com.jg.sistema.booking.infrastructure.persistence.entities.BookingDetailEntity;
import com.jg.sistema.booking.infrastructure.persistence.entities.BookingEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BookingDetailMapper {

    public static BookingDetail toDomain(BookingDetailEntity entity) {
        if (entity == null) return null;
        return new BookingDetail(
                entity.getId(),
                entity.getBooking() != null ? entity.getBooking().getId() : null,
                entity.getSeatLabel(),
                entity.getTicketType(),
                entity.getPrice()
        );
    }

    public static BookingDetailEntity toEntity(BookingDetail domain, BookingEntity bookingEntity) {
        if (domain == null) return null;
        return BookingDetailEntity.builder()
                .id(domain.id())
                .booking(bookingEntity)
                .seatLabel(domain.seatLabel())
                .ticketType(domain.ticketType())
                .price(domain.price())
                .build();
    }

    public static List<BookingDetail> toDomainList(List<BookingDetailEntity> entities) {
        if (entities == null || entities.isEmpty()) return Collections.emptyList();
        return entities.stream().map(BookingDetailMapper::toDomain).collect(Collectors.toList());
    }

    public static List<BookingDetailResponseDto> toResponseList(List<BookingDetail> details) {
        if (details == null || details.isEmpty()) return Collections.emptyList();
        return details.stream().map(BookingDetailMapper::toResponse).collect(Collectors.toList());
    }

    public static BookingDetailResponseDto toResponse(BookingDetail domain) {
        if (domain == null) return null;
        return new BookingDetailResponseDto(
                domain.id(),
                domain.seatLabel(),
                domain.ticketType(),
                domain.price()
        );
    }
}
