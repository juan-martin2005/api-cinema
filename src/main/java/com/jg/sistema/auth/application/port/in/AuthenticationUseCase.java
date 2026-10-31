package com.jg.sistema.auth.application.port.in;


import com.jg.sistema.auth.application.command.TokenPair;


public interface AuthenticationUseCase {
    TokenPair authenticate(String email, String password);
}
