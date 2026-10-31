package com.jg.sistema.auth.application.service;

import com.jg.sistema.auth.application.port.in.SendCodeEmailUseCase;
import com.jg.sistema.auth.application.port.in.ResendEmailVerificationCodeUseCase;
import com.jg.sistema.auth.application.port.in.VerifyEmailUseCase;
import com.jg.sistema.auth.application.port.out.EmailSenderPort;
import com.jg.sistema.auth.domain.exception.VerifyCodeException;
import com.jg.sistema.auth.domain.model.EmailVerification;
import com.jg.sistema.user.application.port.out.UserRepositoryPort;
import com.jg.sistema.user.domain.exception.UserNotFoundException;
import com.jg.sistema.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class EmailService implements SendCodeEmailUseCase, VerifyEmailUseCase, ResendEmailVerificationCodeUseCase {
    private final java.time.Clock clock;

    private final EmailSenderPort emailSender;
    private final UserRepositoryPort userRepository;
    private final AuthRateLimiter limiter;

    @Override
    @Transactional
    public void sendCodeToEmail(String email) {
        email = email.strip().toLowerCase(java.util.Locale.ROOT);
        limiter.check("resend:"+email,1,java.time.Duration.ofMinutes(1));

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        emailSender.deleteEmailVerificationByEmail(user.email());

        String code = getOTP();
        String subject = "VERIFICATION CODE";
        String body = "Your verification code is: " + code + " this code expires in 5 minutes.";
        emailSender.sendEmail(email, subject, body);

        EmailVerification emailVerification = new EmailVerification(
                null,
                user.id(),
                code,
                email,
                LocalDateTime.now(clock).plusMinutes(5)
        );
        emailSender.saveEmailVerification(emailVerification);
    }

    @Override
    @Transactional
    public void verifyEmail(String email, String code) {
        email = email.strip().toLowerCase(java.util.Locale.ROOT);
        EmailVerification ev = emailSender.getEmailVerificationByEmail(email)
                .orElseThrow(() -> new VerifyCodeException("No verification code found for this email."));
        limiter.check("verify:"+email+":"+ev.id(),5,java.time.Duration.ofMinutes(5));
        if (!ev.code().equals(code)) {
            throw new VerifyCodeException("The verification code is invalid.");
        }
        if (LocalDateTime.now(clock).isAfter(ev.expiresAt())) {
            throw new VerifyCodeException("The verification code has expired.");
        }
        emailSender.verifyEmail(email);
        emailSender.deleteEmailVerificationById(ev.id());
    }

    @Override
    @Transactional
    public void resendEmailVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (Boolean.TRUE.equals(user.isVerified())) {
            throw new VerifyCodeException("This account is already verified.");
        }

        sendCodeToEmail(email);
    }

    private String getOTP() {
        String numbers = "0123456789";
        int len = 6;
        SecureRandom random = new SecureRandom();
        char[] otp = new char[len];
        for (int i = 0; i < len; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return new String(otp);
    }
}
