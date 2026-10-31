package com.jg.sistema.auth.infrastructure.persistence;

import com.jg.sistema.auth.application.port.out.RefreshTokenPort;
import com.jg.sistema.auth.domain.model.RefreshToken;
import com.jg.sistema.auth.infrastructure.persistence.entities.RefreshTokenEntity;
import com.jg.sistema.auth.infrastructure.persistence.mapper.RefreshTokenMapper;
import com.jg.sistema.auth.infrastructure.persistence.repository.PostgreSqlRefreshTokenRepository;
import com.jg.sistema.user.domain.exception.UserNotFoundException;
import com.jg.sistema.user.infrastructure.persistence.entities.UserEntity;
import com.jg.sistema.user.infrastructure.persistence.repositories.PostgreSqlUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaRefreshTokenAdapter implements RefreshTokenPort {

    private final PostgreSqlRefreshTokenRepository refreshTokenRepository;
    private final PostgreSqlUserRepository userRepository;

    @Override
    public Optional<RefreshToken> findById(Long id) {
        return refreshTokenRepository.findById(id)
                .map(RefreshTokenMapper::mapToDomain);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        String hasToken = hash256(token);
        return refreshTokenRepository.findByToken(hasToken)
                .map(RefreshTokenMapper::mapToDomain);
    }

    @Override
    public Optional<RefreshToken> findByUserId(UUID userId) {
        return refreshTokenRepository.findByUser_Id(userId)
                .map(RefreshTokenMapper::mapToDomain);
    }

    @Override
    @Transactional
    public void save(RefreshToken refreshToken) {
        UserEntity userEntity = userRepository.findById(refreshToken.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + refreshToken.userId()));

        RefreshTokenEntity entity = RefreshTokenMapper.mapToEntity(refreshToken, userEntity);
        entity.setToken( this.hash256(entity.getToken()));
        refreshTokenRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteByUserId(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        String hasToken = hash256(token);
        refreshTokenRepository.deleteByToken(hasToken);
    }

    private String hash256(String token) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = sha.digest(token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
