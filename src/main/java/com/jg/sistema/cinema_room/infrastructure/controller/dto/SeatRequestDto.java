package com.jg.sistema.cinema_room.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SeatRequestDto(
        @NotBlank(message = "row letter of seat is required")
        @jakarta.validation.constraints.Pattern(regexp="[A-Z]{1,3}") String rowLetter,
        @NotNull(message = "number of seat is required")
        @jakarta.validation.constraints.Positive Integer number,
        @NotNull(message = "position X of seat letter is required")
        @jakarta.validation.constraints.PositiveOrZero Integer posX,
        @NotNull(message = "position Y of seat letter is required")
        @jakarta.validation.constraints.PositiveOrZero Integer posY
) {
    public SeatRequestDto { if(rowLetter != null) rowLetter = rowLetter.strip().toUpperCase(java.util.Locale.ROOT); }
}
