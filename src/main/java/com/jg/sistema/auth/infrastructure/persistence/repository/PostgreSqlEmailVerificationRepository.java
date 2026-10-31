package com.jg.sistema.auth.infrastructure.persistence.repository;

import com.jg.sistema.auth.infrastructure.persistence.entities.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostgreSqlEmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Long> {
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    Optional<EmailVerificationEntity> findByEmail(String email);

    void deleteByEmail(String email);
}
