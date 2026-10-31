package com.jg.sistema.show_time.domain.model;

import com.jg.sistema.cinema_room.domain.model.CinemaRoom;
import com.jg.sistema.movie.domain.model.Movie;
import com.jg.sistema.show_time.domain.exception.InvalidTimeException;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShowTime(
        UUID id,
        Movie movie,
        CinemaRoom cinemaRoom,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    private static final int OPENING_HOUR  = 10;
    private static final int CLOSING_HOUR  = 23;
    public ShowTime {
        validateBusinessHours(startTime);
    }

    private static void validateBusinessHours(LocalDateTime start) {
        if (start != null && start.getHour() < OPENING_HOUR) {
            throw new InvalidTimeException("Show times must start at or after %d:00 am".formatted(OPENING_HOUR));
        }
    }

    public static ShowTime withId(UUID id) {
        return new ShowTime(id, null, null, null, null);
    }

    public ShowTime create(Movie movie, CinemaRoom cinemaRoom, LocalDateTime startTime) {
        LocalDateTime calculatedEndTime = startTime.plusMinutes(movie.duration());
        return new ShowTime(
                id,
                movie,
                cinemaRoom,
                startTime,
                calculatedEndTime
        );
    }

    public ShowTime update(Movie movie, CinemaRoom cinemaRoom, LocalDateTime startTime) {
        LocalDateTime calculatedEndTime = startTime.plusMinutes(movie.duration());
        return new ShowTime(
                id,
                movie,
                cinemaRoom,
                startTime,
                calculatedEndTime
        );
    }
}
