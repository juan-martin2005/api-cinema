package com.jg.sistema.cinema_room.infrastructure.controller.dto;

import java.util.UUID;

public record SeatResponseDto(
        UUID id,
        String row,
        Integer number,
        Integer posX,
        Integer posY
) {
}
