package com.jg.sistema.auth.infrastructure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthRequestDTO(
        @Schema(example = "example@gmail.com", description = "The email of the user", requiredMode = Schema.RequiredMode.REQUIRED)
        @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Email String email,
        @Schema(example = "password1234", description = "The password of the user", requiredMode = Schema.RequiredMode.REQUIRED)
        @jakarta.validation.constraints.NotBlank String password
) {
}
