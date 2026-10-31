package com.jg.sistema.show_time.domain.exception;

public class ShowTimeAlreadyExistException extends RuntimeException {
    public ShowTimeAlreadyExistException(String message) {
        super(message);
    }
}
