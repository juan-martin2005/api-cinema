package com.jg.sistema.user.infrastructure.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UserAdminRequestDTO(
        @NotBlank String name,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        @NotBlank @jakarta.validation.constraints.Size(min = 8, max = 72) String password,
        @jakarta.validation.constraints.NotEmpty Set<@NotBlank String>roles) {
}
