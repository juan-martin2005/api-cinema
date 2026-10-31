package com.jg.sistema.booking.infrastructure.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record BookingRequestDto(
        @NotNull(message = "ShowTime ID is required")
        UUID showTimeId,

        @NotEmpty(message = "At least one seat must be selected")
        @jakarta.validation.constraints.Size(max = 10) List<@NotNull UUID> seatIds
) {
}
