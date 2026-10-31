package com.jg.sistema.user.infrastructure.persistence.mapper;

import com.jg.sistema.user.domain.model.Role;
import com.jg.sistema.user.domain.model.User;
import com.jg.sistema.user.infrastructure.controller.dto.UserAdminRequestDTO;
import com.jg.sistema.user.infrastructure.controller.dto.UserRequestDTO;
import com.jg.sistema.user.infrastructure.controller.dto.UserResponseDTO;
import com.jg.sistema.user.infrastructure.persistence.entities.RoleEntity;
import com.jg.sistema.user.infrastructure.persistence.entities.UserEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserEntity mapToEntity(User user) {

        Set<RoleEntity> roleEntities = user.roles().stream()
                .map( role -> new RoleEntity(role.id(), role.name()))
                .collect(Collectors.toSet());

        return UserEntity.builder()
                .id(user.id())
                .name(user.name())
                .lastName(user.lastName())
                .email(user.email())
                .password(user.password())
                .isActive(user.isActive())
                .roles(roleEntities)
                .isVerified(user.isVerified())
                .createdAt(user.createdAt())
                .updatedAt(user.updatedAt())
                .build();
    }
    public static User mapToDomain(UserEntity entity) {

        Set<Role> roles = entity.getRoles().stream()
                .map(roleEntity -> new Role(roleEntity.getId(), roleEntity.getName()))
                .collect(Collectors.toSet());
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getIsActive(),
                roles,
                entity.getIsVerified(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static User mapToDomain(UserAdminRequestDTO userRequest) {
        Set<Role> roles = userRequest.roles().stream()
                .map(roleName -> new Role(null, roleName))
                .collect(Collectors.toSet());
        return new User(
                null,
                userRequest.name(),
                userRequest.lastName(),
                userRequest.email(),
                userRequest.password(),
                null,
                roles,
                null,
                null,
                null
        );
    }

    public static User mapToDomain(UserRequestDTO userRequest) {

        return new User(
                null,
                userRequest.name(),
                userRequest.lastName(),
                userRequest.email(),
                userRequest.password(),
                null,
                null,
                null,
                null,
                null
        );
    }

    public static UserResponseDTO mapToResponseDTO(User user) {
        Set<String> roles = user.roles().stream()
                .map(Role::name)
                .collect(Collectors.toSet());

        return new UserResponseDTO(
                user.id(),
                user.name(),
                user.lastName(),
                user.email(),
                user.isActive(),
                roles,
                user.isVerified()
        );
    }
}
