package com.jg.sistema.user.infrastructure.controller.dto;

import java.util.Set;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String lastName,
        String email,
        Boolean isActive,
        Set<String> roles,
        Boolean isVerified
        ) {
}
