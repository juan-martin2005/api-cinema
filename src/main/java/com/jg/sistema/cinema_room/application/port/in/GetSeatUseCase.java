package com.jg.sistema.cinema_room.application.port.in;

import com.jg.sistema.cinema_room.domain.model.Seat;

import java.util.List;
import java.util.UUID;

public interface GetSeatUseCase {
    List<Seat> getSeatsByCinemaRoom(UUID cinemaRoomId);
    Seat getSeatById(UUID seatId);
}
