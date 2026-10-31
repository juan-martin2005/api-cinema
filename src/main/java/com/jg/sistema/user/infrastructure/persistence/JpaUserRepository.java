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

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepositoryPort {

    private final PostgreSqlUserRepository userRepository;

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
        return userRepository.findByEmail(email).map(UserMapper::mapToDomain);
    }

    @Override
    public Optional<User> findByEmailWithRoles(String email) {
        return userRepository.findByEmailWithRoles(email).map(UserMapper::mapToDomain);
    }

    @Override
    public void save(User user) {
        UserEntity userEntity = UserMapper.mapToEntity(user);
        userRepository.save(userEntity);
    }

}
