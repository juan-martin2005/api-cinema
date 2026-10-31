package com.jg.sistema.user.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeUserPasswordDto(
       @NotBlank String currentPassword,
       @NotBlank String newPassword,
       @NotBlank String confirmNewPassword
) {
}
