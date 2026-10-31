package com.jg.sistema.auth.infrastructure.persistence;

import com.jg.sistema.auth.application.port.out.EmailSenderPort;
import com.jg.sistema.auth.domain.model.EmailVerification;
import com.jg.sistema.auth.infrastructure.persistence.entities.EmailVerificationEntity;
import com.jg.sistema.auth.infrastructure.persistence.mapper.EmailVerificationMapper;
import com.jg.sistema.auth.infrastructure.persistence.repository.PostgreSqlEmailVerificationRepository;
import com.jg.sistema.user.domain.exception.UserNotFoundException;
import com.jg.sistema.user.infrastructure.persistence.entities.UserEntity;
import com.jg.sistema.user.infrastructure.persistence.repositories.PostgreSqlUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JpaMailSenderAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;
    private final PostgreSqlEmailVerificationRepository emailVerificationRepository;
    private final PostgreSqlUserRepository userRepository;

    @Value("${spring.mail.username}")
    private String fronEmail;

    @Override
    public Optional<EmailVerification> getEmailVerificationByEmail(String email) {
        return emailVerificationRepository.findByEmail(email).map(EmailVerificationMapper::mapToDomain);
    }

    @Override
    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fronEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        }catch (Exception ex){
            log.error("Failed to send email to {}: {}", to, ex.getMessage());
        }
    }

    @Override
    public void saveEmailVerification(EmailVerification emailVerification) {
        UserEntity userEntity = userRepository.getReferenceById(emailVerification.userId());
        EmailVerificationEntity entity = EmailVerificationMapper.mapToEntity(emailVerification);
        entity.setUser(userEntity);
        emailVerificationRepository.save(entity);
    }

    @Override
    public void verifyEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        userEntity.setIsVerified(true);
        userEntity.setIsActive(true);
        userRepository.save(userEntity);
    }

    @Override
    public void deleteEmailVerificationById(Long id) {
        emailVerificationRepository.deleteById(id);
    }

    @Override
    public void deleteEmailVerificationByEmail(String email) {
        emailVerificationRepository.deleteByEmail(email);
    }


}
