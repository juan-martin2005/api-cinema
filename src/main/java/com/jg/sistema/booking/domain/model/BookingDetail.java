package com.jg.sistema.booking.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record BookingDetail(
        UUID id,
        UUID bookingId,
        String seatLabel,
        String ticketType,
        BigDecimal price
) {
}
