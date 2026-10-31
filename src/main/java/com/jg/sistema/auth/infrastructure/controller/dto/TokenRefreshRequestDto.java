package com.jg.sistema.auth.infrastructure.controller.dto;

public record TokenRefreshRequestDto(
        @jakarta.validation.constraints.NotBlank String refreshToken
) {
}
