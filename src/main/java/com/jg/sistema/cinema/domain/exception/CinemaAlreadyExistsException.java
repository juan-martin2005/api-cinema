package com.jg.sistema.cinema.domain.exception;

public class CinemaAlreadyExistsException extends RuntimeException {
    public CinemaAlreadyExistsException(String message) {
        super(message);
    }
}
