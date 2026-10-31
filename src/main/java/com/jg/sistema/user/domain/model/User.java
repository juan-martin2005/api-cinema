package com.jg.sistema.user.domain.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record User(
        UUID id,
        String name,
        String lastName,
        String email,
        String password,
        Boolean isActive,
        Set<Role> roles,
        Boolean isVerified,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    private User copy(String password, Boolean isActive, LocalDateTime updatedAtAt) {
        return new User(
                id,
                name,
                lastName,
                email,
                password,
                isActive,
                roles,
                isVerified,
                createdAt,
                updatedAtAt
        );
    }

    public User create(UUID id, String name, String lastName, String email, String password, Set<Role> roles) {
        return new User(
                id,
                name,
                lastName,
                email,
                password,
                true,
                roles,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public User register(UUID id, String name, String lastName, String email, String password, Set<Role> roles) {
        return new User(
                id,
                name,
                lastName,
                email,
                password,
                isActive,
                roles,
                isVerified,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public User changePassword(String newPassword) {
        return copy(newPassword, isActive, LocalDateTime.now());
    }

    public User activate() {
        if(isActive) return this;

        return copy(password, true, LocalDateTime.now());
    }
    public User deactivate() {
        if(!isActive) return this;
        return copy(password, false, LocalDateTime.now());
    }
}
