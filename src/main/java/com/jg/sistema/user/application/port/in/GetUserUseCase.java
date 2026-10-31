package com.jg.sistema.user.application.port.in;

import com.jg.sistema.user.domain.model.User;

import java.util.List;
import java.util.UUID;

public interface GetUserUseCase {
    List<User> findAll();
    User findById(UUID id);
    User findByEmail(String email);
}
