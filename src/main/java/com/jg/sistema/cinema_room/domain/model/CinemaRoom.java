package com.jg.sistema.cinema_room.domain.model;

import com.jg.sistema.cinema.domain.model.Cinema;

import java.util.List;
import java.util.UUID;

public record CinemaRoom(
        UUID id,
        String name,
        Integer capacity,
        List<Seat> seats,
        Cinema cinema
) {


    public static CinemaRoom withId(UUID id) {
        return new CinemaRoom(id, null, null, null, null);
    }

    public CinemaRoom create(String name, Integer capacity, Cinema cinema) {
        return new CinemaRoom(id, name, capacity, seats, cinema);
    }

    public CinemaRoom update(String name) {
        return new CinemaRoom(id, name, capacity, seats, cinema);
    }
}
