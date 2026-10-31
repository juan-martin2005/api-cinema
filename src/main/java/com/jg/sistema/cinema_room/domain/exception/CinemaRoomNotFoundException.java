package com.jg.sistema.cinema_room.domain.exception;

public class CinemaRoomNotFoundException extends RuntimeException {
    public CinemaRoomNotFoundException(String message) {
        super(message);
    }
}
