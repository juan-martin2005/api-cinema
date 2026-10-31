package com.jg.sistema.auth.infrastructure.persistence.entities;

import com.jg.sistema.user.infrastructure.persistence.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "email_verifications")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailVerificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private String code;
    private String email;
    private LocalDateTime expiresAt;
}
