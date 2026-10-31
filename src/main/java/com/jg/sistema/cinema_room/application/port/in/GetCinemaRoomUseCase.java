package com.jg.sistema.cinema_room.application.port.in;

import com.jg.sistema.cinema_room.domain.model.CinemaRoom;

import java.util.List;
import java.util.UUID;

public interface GetCinemaRoomUseCase {
    List<CinemaRoom> getCinemaRooms();
    CinemaRoom getCinemaRoomById(UUID id);
}
