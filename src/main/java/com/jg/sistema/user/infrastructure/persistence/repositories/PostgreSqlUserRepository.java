package com.jg.sistema.user.infrastructure.persistence.repositories;

import com.jg.sistema.user.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PostgreSqlUserRepository extends JpaRepository<UserEntity, UUID> {
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "roles")
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.email = :email ")
    Optional<UserEntity> findByEmailWithRoles(String email);

}
