package com.jg.sistema.shared.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;

@Component @Profile("dev") @RequiredArgsConstructor
public class DemoAdminConfig implements ApplicationRunner {
    private final JdbcTemplate jdbc;
    private final PasswordEncoder passwords;
    @Value("${DEMO_ADMIN_PASSWORD:}") private String password;
    public void run(ApplicationArguments args) {
        if(password.isBlank()) return;
        if(password.length()<8 || password.length()>72) throw new IllegalArgumentException("DEMO_ADMIN_PASSWORD must contain 8 to 72 characters");
        jdbc.update("UPDATE users SET password=?,session_version=session_version+1 WHERE email='admin@example.test'",passwords.encode(password));
    }
}
