package com.jg.sistema.auth.application.service;

import com.jg.sistema.auth.application.command.TokenPair;
import com.jg.sistema.auth.application.port.in.RefreshTokenUseCase;
import com.jg.sistema.auth.application.port.out.RefreshTokenPort;
import com.jg.sistema.auth.application.port.out.TokenGeneratorPort;
import com.jg.sistema.auth.domain.exception.InvalidCredentialException;
import com.jg.sistema.auth.domain.exception.RefreshTokenExpiredException;
import com.jg.sistema.auth.domain.exception.RefreshTokenNotFoundException;
import com.jg.sistema.auth.domain.model.RefreshToken;
import com.jg.sistema.user.application.port.out.UserRepositoryPort;
import com.jg.sistema.user.domain.exception.UserNotFoundException;
import com.jg.sistema.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService implements RefreshTokenUseCase {

    private final TokenGeneratorPort tokenGeneratorPort;
    private final UserRepositoryPort userRepositoryPort;
    private final RefreshTokenPort refreshTokenPort;

    @Value("${spring.security.jwt.refresh-expiration}")
    private Long expiration;

    @Override
    public String createRefreshToken(UUID userId) {

        userRepositoryPort.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found"));

        String refreshToken = UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken(
                null,
                refreshToken,
                userId,
                Instant.now().plusMillis(expiration)
        );
       refreshTokenPort.save(token);

       return refreshToken;
    }

    @Override
    public String createRefreshTokenOauth2(String email) {
        User userOauth = userRepositoryPort.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found"));
        return createRefreshToken(userOauth.id());
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.isExpired()){
            refreshTokenPort.deleteByUserId(token.userId());
            throw new RefreshTokenExpiredException("The refresh token has expired");
        }
        return token;
    }

    @Override
    public TokenPair refreshAccessToken(String refreshToken) {
        return refreshTokenPort.findByToken(refreshToken)
                .map(this::verifyExpiration)
                .map(rt ->{
                    User user = userRepositoryPort.findById(rt.userId()).orElseThrow(()-> new UserNotFoundException("User not found"));

                    if(!user.isActive() || !user.isVerified()){
                        refreshTokenPort.deleteByUserId(user.id());
                        throw new InvalidCredentialException("User is disable or not verified");
                    }

                    String newAccessToken = tokenGeneratorPort.generateToken(user.email());

                    refreshTokenPort.deleteByToken(refreshToken);

                    String newRefreshToken = this.createRefreshToken(user.id());

                    return new TokenPair(newAccessToken, newRefreshToken);

                } )
                .orElseThrow(()-> new RefreshTokenNotFoundException("Token refresh not found"));
    }

    @Override
    public void deleteRefreshTokenByUser(UUID userId) {
        refreshTokenPort.deleteByUserId(userId);
    }

    @Override
    public void deleteRefreshTokenByToken(String refreshToken) {
        refreshTokenPort.deleteByToken(refreshToken);
    }
}
