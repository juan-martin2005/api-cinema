package com.jg.sistema.show_time.infrastructure.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShowTimeRequestDto(
        @NotNull(message = "Movie ID cannot be null") UUID movieId,
        @NotNull(message = "Cinema room ID cannot be null") UUID cinemaRoomId,
        @NotNull(message = "Start time cannot be null") LocalDateTime startTime
) {
}
