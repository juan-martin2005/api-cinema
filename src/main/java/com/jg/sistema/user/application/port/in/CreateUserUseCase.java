package com.jg.sistema.user.application.port.in;

import com.jg.sistema.user.domain.model.User;

public interface CreateUserUseCase {
    void createUser(User user);
}
