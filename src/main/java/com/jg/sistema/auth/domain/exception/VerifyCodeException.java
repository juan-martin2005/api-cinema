package com.jg.sistema.auth.domain.exception;

public class VerifyCodeException extends RuntimeException {
    public VerifyCodeException(String message) {
        super(message);
    }
}
