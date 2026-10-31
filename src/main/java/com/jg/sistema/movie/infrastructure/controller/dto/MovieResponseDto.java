package com.jg.sistema.movie.infrastructure.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MovieResponseDto(
        UUID id,
        String title,
        String description,
        String posterUrl,
        String classification,
        Integer duration,
        String genre,
        List<FunctionCinemaResponse> cinemas,
        boolean isNewRelease,
        boolean isPreSale,
        LocalDateTime openingDate
) {
    public MovieResponseDto(UUID id, String title, String description, String posterUrl, String classification, Integer duration, String genre) {
        this(id, title, description, posterUrl, classification, duration, genre, null, false, false, null);
    }

}
