package com.jg.sistema.cinema.infrastructure.controller.dto;

import java.util.Set;
import java.util.UUID;

public record CinemaResponseDto(
        UUID id,
        String name,
        String img,
        String address,
        String secondAddress,
        String city,
        Double latitude,
        Double longitude,
        Set<FormatResponseDto> formats,
        Integer roomQuantity,
        String description
) {
}
