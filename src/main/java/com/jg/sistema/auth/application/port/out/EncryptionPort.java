package com.jg.sistema.auth.application.port.out;

public interface EncryptionPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
