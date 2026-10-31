package com.jg.sistema.booking.infrastructure.controller.dto;

import com.jg.sistema.booking.domain.model.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BookingResponseDto(
        UUID id,
        String code,
        UUID showTimeId,
        UUID userId,
        BookingStatus bookingStatus,
        com.jg.sistema.booking.domain.model.PaymentStatus paymentStatus,
        BigDecimal subTotal,
        BigDecimal tax,
        BigDecimal total,
        LocalDateTime creatAt,
        LocalDateTime expiredAt,
        String currency,
        List<BookingDetailResponseDto> details
) {
}
