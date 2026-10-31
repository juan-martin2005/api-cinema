package com.jg.sistema.user.application.port.out;

import com.jg.sistema.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    List<User> findAll();
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailWithRoles(String email);
    void save(User user);
}
