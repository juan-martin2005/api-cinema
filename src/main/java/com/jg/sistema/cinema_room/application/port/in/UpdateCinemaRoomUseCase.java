package com.jg.sistema.cinema_room.application.port.in;

import com.jg.sistema.cinema_room.domain.model.CinemaRoom;

import java.util.UUID;

public interface UpdateCinemaRoomUseCase {
    void updateCinemaRoom(UUID id, CinemaRoom cinemaRoom);
}
