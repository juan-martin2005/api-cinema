package com.jg.sistema.auth.application.port.out;

import java.util.List;

public interface TokenGeneratorPort {
    String generateToken(String subject);
    boolean validateToken(String token);
    List<String> extractRoles(String token);
    String extractSubject(String token);

}
