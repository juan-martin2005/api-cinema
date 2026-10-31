package com.jg.sistema.auth.infrastructure.config;

import com.jg.sistema.auth.infrastructure.persistence.mapper.UserDetailCustom;
import com.jg.sistema.user.domain.exception.UserNotFoundException;
import com.jg.sistema.user.infrastructure.persistence.repositories.PostgreSqlUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {

    private final PostgreSqlUserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return  username -> new UserDetailCustom(userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found")));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

}
