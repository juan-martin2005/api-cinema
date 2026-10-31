package com.jg.sistema.auth.application.port.in;

public interface VerifyEmailUseCase {
    void verifyEmail(String email, String code);
}
