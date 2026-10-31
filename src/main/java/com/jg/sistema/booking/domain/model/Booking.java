package com.jg.sistema.booking.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record Booking(
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
        List<BookingDetail> details
) {
    public Booking(
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
            LocalDateTime expiredAt
    ) {
        this(id, code, showTimeId, userId, bookingStatus, paymentStatus, subTotal, tax, total, creatAt, expiredAt, Collections.emptyList());
    }

    public Booking withDetails(List<BookingDetail> details) {
        return new Booking(
                id,
                code,
                showTimeId,
                userId,
                bookingStatus,
                paymentStatus,
                subTotal,
                tax,
                total,
                creatAt,
                expiredAt,
                details
        );
    }

    public Booking withStatus(BookingStatus newStatus, PaymentStatus newPaymentStatus) {
        return new Booking(
                id,
                code,
                showTimeId,
                userId,
                newStatus,
                newPaymentStatus,
                subTotal,
                tax,
                total,
                creatAt,
                expiredAt,
                details
        );
    }
}
