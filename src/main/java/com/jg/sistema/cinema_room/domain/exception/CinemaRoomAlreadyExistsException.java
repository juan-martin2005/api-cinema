package com.jg.sistema.cinema_room.domain.exception;

public class CinemaRoomAlreadyExistsException extends RuntimeException {
    public CinemaRoomAlreadyExistsException(String message) {
        super(message);
    }
}
