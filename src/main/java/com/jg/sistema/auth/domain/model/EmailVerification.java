package com.jg.sistema.auth.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record EmailVerification(
        Long id,
        UUID userId,
        String code,
        String email,
        LocalDateTime expiresAt
) {
}
