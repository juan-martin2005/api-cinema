package com.jg.sistema.user.infrastructure.controller.dto;

public record UserProfileDto(
        String name,
        String lastName,
        String email
) {
}
