package com.jg.sistema.user.infrastructure.controller;

import com.jg.sistema.user.application.command.ChangePasswordCommand;
import com.jg.sistema.user.application.port.in.CreateUserUseCase;
import com.jg.sistema.user.application.port.in.GetUserUseCase;
import com.jg.sistema.user.application.port.in.UpdateUserUseCase;
import com.jg.sistema.user.domain.model.User;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import com.jg.sistema.user.infrastructure.contract.ApiUserDoc;
import com.jg.sistema.user.infrastructure.controller.dto.ChangeUserPasswordDto;
import com.jg.sistema.user.infrastructure.controller.dto.UserAdminRequestDTO;
import com.jg.sistema.user.infrastructure.controller.dto.UserResponseDTO;
import com.jg.sistema.user.infrastructure.persistence.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController implements ApiUserDoc {

    private final GetUserUseCase getUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponseDTO> getAllUsers(){
        return getUserUseCase.findAll()
                .stream()
                .map(UserMapper::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponseDTO getUserById(@PathVariable UUID id) {
         User user = getUserUseCase.findById(id);
         return UserMapper.mapToResponseDTO(user);
    }

    @Override
    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserAdminRequestDTO userRequest) {
        User user = UserMapper.mapToDomain(userRequest);
        createUserUseCase.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("User created successfully",true));
    }


    @Override
    @PatchMapping("{id}/toggle-status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> toggleUserStatus(@PathVariable UUID id) {
        updateUserUseCase.toggleUserStatus(id);
        return ResponseEntity.ok(new ApiResponseDTO("User status toggled successfully", true));
    }

    @Override
    @PatchMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changeUserPassword(@Valid @RequestBody ChangeUserPasswordDto changePassword) {
        ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand(
                changePassword.currentPassword(),
                changePassword.newPassword(),
                changePassword.confirmNewPassword()
        );
        updateUserUseCase.changePassword(changePasswordCommand);
        return ResponseEntity.ok(new ApiResponseDTO("User password change successfully", true));
    }

}
