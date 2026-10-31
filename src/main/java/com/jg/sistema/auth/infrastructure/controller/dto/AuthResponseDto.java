package com.jg.sistema.auth.infrastructure.controller.dto;

public record AuthResponseDto(
        String accessToken,
        String refreshToken
) {
}
