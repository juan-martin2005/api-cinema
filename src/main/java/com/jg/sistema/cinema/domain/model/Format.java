package com.jg.sistema.cinema.domain.model;

import java.math.BigDecimal;

public record Format(
        Long id,
        String name,
        BigDecimal price
) {
    public static Format withId(Long id) {
        return new Format(id, null, null);
    }
}
