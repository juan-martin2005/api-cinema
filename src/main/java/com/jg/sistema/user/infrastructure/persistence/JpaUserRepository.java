package com.jg.sistema.user.infrastructure.persistence;

import com.jg.sistema.user.application.port.out.UserRepositoryPort;
import com.jg.sistema.user.domain.model.User;
import com.jg.sistema.user.infrastructure.persistence.entities.UserEntity;
import com.jg.sistema.user.infrastructure.persistence.mapper.UserMapper;
import com.jg.sistema.user.infrastructure.persistence.repositories.PostgreSqlUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@org.springframework.transaction.annotation.Transactional(readOnly = true)
@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepositoryPort {

    private final PostgreSqlUserRepository userRepository;
    private final org.springframework.jdbc.core.JdbcTemplate jdbc;
    public long sessionVersion(String email) { return jdbc.queryForObject("SELECT session_version FROM users WHERE email=?",Long.class,email.strip().toLowerCase(java.util.Locale.ROOT)); }
    @org.springframework.transaction.annotation.Transactional
    public void revokeSessions(UUID userId) {
        jdbc.update("UPDATE users SET session_version=session_version+1 WHERE id=?",userId);
        jdbc.update("DELETE FROM refresh_token WHERE user_id=?",userId);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::mapToDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.strip().toLowerCase(java.util.Locale.ROOT)).map(UserMapper::mapToDomain);
    }

    @Override
    public Optional<User> findByEmailWithRoles(String email) {
        return userRepository.findByEmailWithRoles(email).map(UserMapper::mapToDomain);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void save(User user) {
        UserEntity userEntity = UserMapper.mapToEntity(user);
        userEntity.setEmail(userEntity.getEmail().strip().toLowerCase(java.util.Locale.ROOT));
        if (user.id() != null) userEntity.setSessionVersion(sessionVersion(user.email()));
        userRepository.saveAndFlush(userEntity);
    }

}
