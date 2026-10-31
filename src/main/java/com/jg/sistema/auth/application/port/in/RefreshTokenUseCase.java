package com.jg.sistema.auth.application.port.in;

import com.jg.sistema.auth.application.command.TokenPair;
import com.jg.sistema.auth.domain.model.RefreshToken;

import java.util.UUID;

public interface RefreshTokenUseCase {
    String createRefreshToken(UUID userId);
    String createRefreshTokenOauth2(String email);
    RefreshToken verifyExpiration(RefreshToken token);
    TokenPair refreshAccessToken(String refreshToken);
    void deleteRefreshTokenByUser(UUID userId);
    void deleteRefreshTokenByToken(String refreshToken);
}
