package com.jg.sistema.auth.infrastructure.persistence.mapper;

import com.jg.sistema.auth.domain.model.RefreshToken;
import com.jg.sistema.auth.infrastructure.persistence.entities.RefreshTokenEntity;
import com.jg.sistema.user.infrastructure.persistence.entities.UserEntity;

public class RefreshTokenMapper {

    public static RefreshTokenEntity mapToEntity(RefreshToken domain, UserEntity userEntity) {
        return new RefreshTokenEntity(
                domain.id(),
                userEntity,
                domain.token(),
                domain.expiryDate()
        );
    }

    public static RefreshToken mapToDomain(RefreshTokenEntity entity) {
        if (entity == null) {
            return null;
        }
        return new RefreshToken(
                entity.getId(),
                entity.getToken(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getExpiryDate()
        );
    }
}
