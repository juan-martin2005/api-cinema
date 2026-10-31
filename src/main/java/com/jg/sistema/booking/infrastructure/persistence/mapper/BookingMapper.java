package com.jg.sistema.booking.infrastructure.persistence.mapper;

import com.jg.sistema.booking.domain.model.Booking;
import com.jg.sistema.booking.domain.model.BookingDetail;
import com.jg.sistema.booking.infrastructure.controller.dto.BookingResponseDto;
import com.jg.sistema.booking.infrastructure.persistence.entities.BookingDetailEntity;
import com.jg.sistema.booking.infrastructure.persistence.entities.BookingEntity;
import com.jg.sistema.show_time.infrastructure.persistence.entities.ShowTimeEntity;
import com.jg.sistema.user.infrastructure.persistence.entities.UserEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {

    public static Booking toDomain(BookingEntity entity) {
        if (entity == null) return null;

        List<BookingDetail> details = entity.getDetails() != null
                ? entity.getDetails().stream().map(BookingDetailMapper::toDomain).collect(Collectors.toList())
                : Collections.emptyList();

        return new Booking(
                entity.getId(),
                entity.getCode(),
                entity.getShowTimeEntity() != null ? entity.getShowTimeEntity().getId() : null,
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getBookingStatus(),
                entity.getPaymentStatus(),
                entity.getSubTotal(),
                entity.getTax(),
                entity.getTotal(),
                entity.getCreatAt(),
                entity.getExpiredAt(),
                details
        );
    }

    public static BookingEntity toEntity(Booking domain) {
        if (domain == null) return null;

        BookingEntity entity = BookingEntity.builder()
                .id(domain.id())
                .code(domain.code())
                .bookingStatus(domain.bookingStatus())
                .paymentStatus(domain.paymentStatus())
                .subTotal(domain.subTotal())
                .tax(domain.tax())
                .total(domain.total())
                .creatAt(domain.creatAt())
                .expiredAt(domain.expiredAt())
                .showTimeEntity(domain.showTimeId() != null
                        ? ShowTimeEntity.builder().id(domain.showTimeId()).build()
                        : null)
                .user(domain.userId() != null
                        ? UserEntity.builder().id(domain.userId()).build()
                        : null)
                .details(new ArrayList<>())
                .build();

        if (domain.details() != null && !domain.details().isEmpty()) {
            for (BookingDetail detail : domain.details()) {
                BookingDetailEntity detailEntity = BookingDetailMapper.toEntity(detail, entity);
                entity.addDetail(detailEntity);
            }
        }

        return entity;
    }

    public static BookingResponseDto toResponse(Booking domain) {
        if (domain == null) return null;

        return new BookingResponseDto(
                domain.id(),
                domain.code(),
                domain.showTimeId(),
                domain.userId(),
                domain.bookingStatus(),
                domain.paymentStatus(),
                domain.subTotal(),
                domain.tax(),
                domain.total(),
                domain.creatAt(),
                domain.expiredAt(),
                "PEN",
                BookingDetailMapper.toResponseList(domain.details())
        );
    }
}
