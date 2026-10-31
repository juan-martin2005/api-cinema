package com.jg.sistema.auth.application.port.in;

public interface ResendEmailVerificationCodeUseCase {
    void resendEmailVerificationCode(String email);
}
