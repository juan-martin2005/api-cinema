package com.jg.sistema.user.application.service;

import com.jg.sistema.auth.application.port.out.EncryptionPort;
import com.jg.sistema.auth.domain.exception.EmailAlreadyExistsException;
import com.jg.sistema.auth.domain.exception.InvalidCredentialException;
import com.jg.sistema.user.application.command.ChangePasswordCommand;
import com.jg.sistema.user.application.port.in.*;
import com.jg.sistema.user.application.port.out.RolRepositoryPort;
import com.jg.sistema.user.application.port.out.UserContextHolderPort;
import com.jg.sistema.user.application.port.out.UserRepositoryPort;
import com.jg.sistema.user.domain.exception.PasswordArgumentException;
import com.jg.sistema.user.domain.exception.RolNotFoundException;
import com.jg.sistema.user.domain.exception.UserNotFoundException;
import com.jg.sistema.user.domain.model.Role;
import com.jg.sistema.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements GetUserUseCase, CreateUserUseCase, RegisterUserUseCase, UpdateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final EncryptionPort encryptionPort;
    private final UserContextHolderPort userContextHolder;
    private final RolRepositoryPort rolRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public void createUser(User user) {
        String password = encryptionPort.encode(user.password());
        userRepository.findByEmail(user.email()).ifPresent(existingUser -> {
            throw new EmailAlreadyExistsException("Email is already in use");
        });
        Set<Role> roles = user.roles().stream()
                .map(role -> rolRepository.findByName(role.name())
                        .orElseThrow(() -> new RolNotFoundException("Role not found: " + role.name())))
                .collect(Collectors.toSet());
        User newUser = user.create(
                user.id(),
                user.name(),
                user.lastName(),
                user.email(),
                password,
                roles
        );
        userRepository.save(newUser);

    }

    @Override
    @Transactional
    public void registerUser(User user) {
        String password = user.password() != null ? encryptionPort.encode(user.password()) : null;
        userRepository.findByEmail(user.email()).ifPresent(existingUser -> {
            throw new EmailAlreadyExistsException("Email is already in use");
        });
        Role role = rolRepository.findByName("USER").orElseThrow(() -> new RolNotFoundException("Default role not found: USER"));
        Set<Role> roles = Set.of(role);
        User newUser = user.register(
                user.id(),
                user.name(),
                user.lastName(),
                user.email(),
                password,
                roles
        );
        userRepository.save(newUser);
    }

    @Override
    public void registerUserOauth2(String email, String name, String lastname) {
        if (userRepository.findByEmail(email).isEmpty()) {
            Role role = rolRepository.findByName("USER").orElseThrow(() -> new RolNotFoundException("Default role not found: USER"));
            Set<Role> roles = Set.of(role);
            User newUser = new User(
                    null,
                    name,
                    lastname,
                    email,
                    null,
                    true,
                    roles,
                    true,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            userRepository.save(newUser);
        }
    }

    @Override
    @Transactional
    public void toggleUserStatus(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        User updatedUser = user.isActive() ? user.deactivate() : user.activate();
        userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordCommand changePassword) {
        UUID id = userContextHolder.getCurrentUserId();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        String newPassword = changePassword.newPassword();

        if (!encryptionPort.matches(changePassword.currentPassword(), user.password())) {
            throw new InvalidCredentialException("Current password is incorrect");
        }

        if(!newPassword.equals(changePassword.confirmNewPassword())) {
            throw new PasswordArgumentException("New password and confirm new password do not match");
        }
        if (encryptionPort.matches(changePassword.newPassword(), user.password())) {
            throw new PasswordArgumentException("New password cannot be the same as current password");
        }

        String encodedPassword = encryptionPort.encode(newPassword);
        userRepository.save(
                user.changePassword(encodedPassword)
        );
    }

}
