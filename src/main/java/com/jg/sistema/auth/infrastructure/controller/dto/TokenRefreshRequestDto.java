package com.jg.sistema.auth.infrastructure.controller.dto;

public record TokenRefreshRequestDto(
        String refreshToken
) {
}
