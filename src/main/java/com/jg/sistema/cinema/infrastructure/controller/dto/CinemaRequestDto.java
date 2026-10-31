package com.jg.sistema.cinema.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Set;

public record CinemaRequestDto(
        @NotBlank(message = "Cinema name is required")
        String name,

        String img,

        @NotBlank(message = "Address is required")
        String address,

        String secondAddress,

        @NotBlank(message = "City is required")
        String city,

        Double latitude,

        Double longitude,

        Set<Long> formatIds,

        @NotNull(message = "Room quantity is required")
        @PositiveOrZero(message = "Room quantity must be zero or positive")
        Integer roomQuantity,

        String description
) {
}
