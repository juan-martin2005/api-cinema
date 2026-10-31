package com.jg.sistema.cinema_room.infrastructure.controller.dto;

import java.util.Set;
import java.util.UUID;

public record CinemaRoomResponseDto(
        UUID id,
        String name,
        UUID cinemaId,
        Set<String> format,
        Set<String> languages
) {
}
