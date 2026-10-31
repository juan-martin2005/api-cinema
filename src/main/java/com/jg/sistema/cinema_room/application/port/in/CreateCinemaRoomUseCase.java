package com.jg.sistema.cinema_room.application.port.in;

import com.jg.sistema.cinema_room.domain.model.CinemaRoom;
import com.jg.sistema.cinema_room.domain.model.Seat;

import java.util.List;

public interface CreateCinemaRoomUseCase {
    void createCinemaRoom(CinemaRoom cinemaRoom, List<Seat> seats);
}
