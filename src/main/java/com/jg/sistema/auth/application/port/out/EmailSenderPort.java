package com.jg.sistema.auth.application.port.out;

import com.jg.sistema.auth.domain.model.EmailVerification;
import java.util.Optional;

public interface EmailSenderPort {
    Optional<EmailVerification> getEmailVerificationByEmail(String email);
    void sendEmail(String email, String subject, String body);
    void saveEmailVerification(EmailVerification emailVerification);
    void verifyEmail(String email);
    void deleteEmailVerificationById(Long id);
    void deleteEmailVerificationByEmail(String email);
}
