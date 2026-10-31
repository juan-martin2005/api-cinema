package com.jg.sistema.auth.infrastructure.persistence.mapper;


import com.jg.sistema.auth.domain.model.EmailVerification;
import com.jg.sistema.auth.infrastructure.persistence.entities.EmailVerificationEntity;

public class EmailVerificationMapper {
    public static EmailVerificationEntity mapToEntity(EmailVerification emailVerification) {
        return new EmailVerificationEntity(
                emailVerification.id(),
                null,
                emailVerification.code(),
                emailVerification.email(),
                emailVerification.expiresAt()
        );
    }
    public static EmailVerification mapToDomain(EmailVerificationEntity entity) {
        return new EmailVerification(
                entity.getId(),
                entity.getUser().getId(),
                entity.getCode(),
                entity.getEmail(),
                entity.getExpiresAt()
        );
    }
}
