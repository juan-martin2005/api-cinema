package com.jg.sistema.auth.infrastructure.security;

import com.jg.sistema.auth.application.port.out.TokenGeneratorPort;
import com.jg.sistema.user.application.port.out.UserRepositoryPort;
import com.jg.sistema.user.infrastructure.controller.dto.UserResponseDTO;
import com.jg.sistema.user.infrastructure.persistence.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtService implements TokenGeneratorPort {

    @Value("${spring.security.jwt.secretKey}")
    private String secret;
    @Value("${spring.security.jwt.expiration}")
    private long expiration;
    private final UserRepositoryPort userRepository;

    @Override
    public String generateToken(String email) {
        try {
            Date expirationToken = new Date(System.currentTimeMillis() + expiration);
            Map<String, Object> claims = getClaims(email);
            return Jwts.builder()
                    .subject(email)
                    .claims(claims)
                    .signWith(getSecretKey())
                    .issuedAt(new Date())
                    .expiration(expirationToken)
                    .compact();

        }catch (JwtException ex){
            throw new IllegalStateException("Unable to sign access token", ex);
        }
    }
    
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (JwtException ex){
            return false;
        }
    }

    @Override
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    @Override
    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Map<String, Object> getClaims(String email) {
        UserResponseDTO user = userRepository.findByEmailWithRoles(email).map(UserMapper::mapToResponseDTO).orElseThrow();
        Map<String, Object> claims = new HashMap<>();
        claims.put("sessionVersion", userRepository.sessionVersion(email));
        claims.put("name", user.name());
        claims.put("lastName", user.lastName());
        claims.put("roles", user.roles());
        claims.put("verified", user.isVerified());
        return claims;
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    @jakarta.annotation.PostConstruct
    void validateConfiguration() { getSecretKey(); }
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }
}
