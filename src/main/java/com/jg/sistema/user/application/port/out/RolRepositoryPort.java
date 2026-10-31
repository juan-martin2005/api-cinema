package com.jg.sistema.user.application.port.out;

import com.jg.sistema.user.domain.model.Role;

import java.util.Optional;

public interface RolRepositoryPort {
    Optional<Role> findByName(String name);
    void createRole(Role role);
}
