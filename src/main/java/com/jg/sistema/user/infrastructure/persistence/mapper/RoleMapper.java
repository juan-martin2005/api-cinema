package com.jg.sistema.user.infrastructure.persistence.mapper;

import com.jg.sistema.user.domain.model.Role;
import com.jg.sistema.user.infrastructure.persistence.entities.RoleEntity;

public class RoleMapper {
    public static Role toDomain(RoleEntity roleEntity) {
        return new Role(roleEntity.getId(), roleEntity.getName());
    }
    public static RoleEntity toEntity(Role role) {
        return new RoleEntity(role.id(), role.name());
    }
}
