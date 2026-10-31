package com.jg.sistema.user.application.port.in;

import com.jg.sistema.user.domain.model.User;

public interface RegisterUserUseCase {
    void registerUser(User user);
    void registerUserOauth2(String email, String name, String lastname);
}
