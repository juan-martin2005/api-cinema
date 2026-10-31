package com.jg.sistema.booking.infrastructure.controller.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BookingDetailResponseDto(
        UUID id,
        String seatLabel,
        String ticketType,
        BigDecimal price
) {
}
