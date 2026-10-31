package com.jg.sistema.cinema_room.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SeatRequestDto(
        @NotBlank(message = "row letter of seat is required")
        String rowLetter,
        @NotNull(message = "number of seat is required")
        Integer number,
        @NotNull(message = "position X of seat letter is required")
        Integer posX,
        @NotNull(message = "position Y of seat letter is required")
        Integer posY
) {
}
