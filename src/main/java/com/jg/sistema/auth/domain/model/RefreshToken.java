package com.jg.sistema.auth.domain.model;

import java.time.Instant;
import java.util.UUID;

public record RefreshToken(
        Long id,
        String token,
        UUID userId,
        Instant expiryDate
) {
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(Instant.now());
    }
}
