package com.jg.sistema.user.infrastructure.persistence;

import com.jg.sistema.user.application.port.out.RolRepositoryPort;
import com.jg.sistema.user.domain.model.Role;
import com.jg.sistema.user.infrastructure.persistence.entities.RoleEntity;
import com.jg.sistema.user.infrastructure.persistence.mapper.RoleMapper;
import com.jg.sistema.user.infrastructure.persistence.repositories.PostgreSqlRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaRolRepository implements RolRepositoryPort {

    private final PostgreSqlRoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name).map(RoleMapper::toDomain);
    }

    @Override
    public void createRole(Role role) {
        RoleEntity roleEntity = RoleMapper.toEntity(role);
        roleRepository.save(roleEntity);
    }
}
