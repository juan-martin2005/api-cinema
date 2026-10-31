package com.jg.sistema.cinema.infrastructure.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record FormatRequestDto(
        @NotBlank(message = "Format name is required")
        String name,

        @NotNull(message = "Format price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Format price must be greater than 0")
        BigDecimal price
) {
}
