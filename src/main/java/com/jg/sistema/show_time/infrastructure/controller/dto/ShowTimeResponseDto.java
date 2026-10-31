package com.jg.sistema.show_time.infrastructure.controller.dto;

import com.jg.sistema.cinema_room.infrastructure.controller.dto.CinemaRoomResponseDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShowTimeResponseDto(
        UUID id,
        CinemaRoomResponseDto rooms,
        LocalDateTime startTime
) {
}
