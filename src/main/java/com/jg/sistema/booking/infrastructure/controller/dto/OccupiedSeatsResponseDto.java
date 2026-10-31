package com.jg.sistema.booking.infrastructure.controller.dto;

import java.util.List;
import java.util.UUID;

public record OccupiedSeatsResponseDto(
        UUID showTimeId,
        List<String> occupiedSeatLabels,
        int occupiedCount
) {
}
