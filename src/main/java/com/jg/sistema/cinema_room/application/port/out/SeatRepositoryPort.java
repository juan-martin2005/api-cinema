package com.jg.sistema.cinema_room.application.port.out;

import com.jg.sistema.cinema_room.domain.model.CinemaRoom;
import com.jg.sistema.cinema_room.domain.model.Seat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepositoryPort {
    List<Seat> findByCinemaRoomId(UUID cinemaRoomId);
    Optional<Seat> findById(UUID seatId);
    void createSeats(List<Seat> seat, CinemaRoom cinemaRoom);
}
