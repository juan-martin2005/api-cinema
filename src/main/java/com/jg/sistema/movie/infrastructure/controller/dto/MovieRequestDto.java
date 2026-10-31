package com.jg.sistema.movie.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MovieRequestDto(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String classification,
        @NotNull Integer duration,
        @NotBlank String genre,
        Boolean isNewRelease,
        Boolean isPreSale,
        LocalDateTime openingDate
) {
    public MovieRequestDto(String title, String description, String classification, Integer duration, String genre) {
        this(title, description, classification, duration, genre, false, false, null);
    }
}
