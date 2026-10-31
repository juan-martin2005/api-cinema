package com.jg.sistema.cinema.infrastructure.controller.dto;

import java.math.BigDecimal;

public record FormatResponseDto(
        Long id,
        String name,
        BigDecimal price
) {
}
