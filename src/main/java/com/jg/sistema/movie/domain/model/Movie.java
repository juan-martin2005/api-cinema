package com.jg.sistema.movie.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Movie(
        UUID id,
        String title,
        String description,
        String posterUrl,
        String classification,
        Integer duration,
        String genre,
        Boolean isNewRelease,
        Boolean isPreSale,
        LocalDateTime openingDate
) {
    public Movie(UUID id, String title, String description, String posterUrl, String classification, Integer duration, String genre) {
        this(id, title, description, posterUrl, classification, duration, genre, false, false, null);
    }

    public static Movie withId(UUID id) {
        return new Movie(id, null, null, null, null, null, null, null, null, null);
    }

    public Movie withPosterUrl(String newPosterUrl) {
        return new Movie(id, title, description, newPosterUrl, classification, duration, genre, isNewRelease, isPreSale, openingDate);
    }

    public Movie withIdAndPoster(UUID newId, String newPosterUrl) {
        return new Movie(newId, title, description, newPosterUrl, classification, duration, genre, isNewRelease, isPreSale, openingDate);
    }
}
