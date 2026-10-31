package com.jg.sistema.cinema_room.application.port.in;

import java.util.UUID;

public interface DeleteCinemaRoomUseCase {
    void  deleteCinemaRoomById(UUID id);
}
