package com.jg.sistema.auth.application.service;

import com.jg.sistema.auth.application.command.TokenPair;
import com.jg.sistema.auth.application.port.in.AuthenticationUseCase;
import com.jg.sistema.auth.application.port.in.RefreshTokenUseCase;
import com.jg.sistema.auth.application.port.out.EncryptionPort;
import com.jg.sistema.auth.application.port.out.RefreshTokenPort;
import com.jg.sistema.auth.application.port.out.TokenGeneratorPort;
import com.jg.sistema.auth.domain.exception.InvalidCredentialException;
import com.jg.sistema.auth.domain.model.RefreshToken;
import com.jg.sistema.auth.infrastructure.controller.dto.AuthResponseDto;
import com.jg.sistema.user.application.port.out.UserRepositoryPort;
import com.jg.sistema.user.application.service.UserService;
import com.jg.sistema.user.domain.exception.UserNotFoundException;
import com.jg.sistema.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {

    private final TokenGeneratorPort tokenGeneratorPort;
    private final EncryptionPort encryptionPort;
    private final UserRepositoryPort userRepositoryPort;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final AuthRateLimiter limiter;

    @Override
    public TokenPair authenticate(String email, String password) {

        limiter.check("login:"+email, 10, java.time.Duration.ofMinutes(15));
        User user = userRepositoryPort.findByEmail(email).orElseThrow(() -> new InvalidCredentialException("Invalid email or password"));

        if (!encryptionPort.matches(password, user.password())) {
            throw new InvalidCredentialException("Invalid email or password");
        }
        if(!user.isVerified() || !user.isActive()) {
            throw new InvalidCredentialException("User email not verified or user is inactive");
        }

        String accessToken = tokenGeneratorPort.generateToken(user.email());
        String refreshToken = refreshTokenUseCase.createRefreshToken(user.id());

        return new TokenPair(accessToken, refreshToken);
    }
}
