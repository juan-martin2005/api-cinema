package com.jg.sistema.auth.application.port.out;

import com.jg.sistema.auth.domain.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenPort {
    Optional<RefreshToken> findById(Long id);
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserId(UUID userId);
    void save(RefreshToken refreshToken);
    void deleteByUserId(UUID userId);
    void deleteByToken(String token);
}
