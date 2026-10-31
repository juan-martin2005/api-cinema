package com.jg.sistema.user.infrastructure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @Schema(example = "john", description = "The name of the user", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String name,
        @Schema(example = "doe", description = "The last name of the user", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String lastName,
        @Schema(example = "john.doe@gmail.com", description = "The email of the user", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Email String email,
        @Schema(example = "password123", description = "The password of the user", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @jakarta.validation.constraints.Size(min = 8, max = 72) String password
        ) {
}
