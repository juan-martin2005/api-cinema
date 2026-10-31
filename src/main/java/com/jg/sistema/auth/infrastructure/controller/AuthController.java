package com.jg.sistema.auth.infrastructure.controller;

import com.jg.sistema.auth.application.command.TokenPair;
import com.jg.sistema.auth.application.port.in.AuthenticationUseCase;
import com.jg.sistema.auth.application.port.in.RefreshTokenUseCase;
import com.jg.sistema.auth.application.port.in.SendCodeEmailUseCase;
import com.jg.sistema.auth.infrastructure.contract.ApiAuthDoc;
import com.jg.sistema.auth.infrastructure.controller.dto.AuthRequestDTO;
import com.jg.sistema.auth.infrastructure.controller.dto.AuthResponseDto;
import com.jg.sistema.auth.infrastructure.controller.dto.TokenRefreshRequestDto;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import com.jg.sistema.user.application.port.in.RegisterUserUseCase;
import com.jg.sistema.user.domain.model.User;
import com.jg.sistema.user.infrastructure.controller.dto.UserRequestDTO;
import com.jg.sistema.user.infrastructure.persistence.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController implements ApiAuthDoc {
    private final AuthenticationUseCase authenticationUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final SendCodeEmailUseCase sendCodeEmailUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO loginRequest) {
        TokenPair tokens = authenticationUseCase.authenticate(loginRequest.email(), loginRequest.password());
        AuthResponseDto response = new AuthResponseDto(tokens.accessToken(), tokens.refreshToken());
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        User user = UserMapper.mapToDomain(userRequestDTO);
        registerUserUseCase.registerUser(user);
        sendCodeEmailUseCase.sendCodeToEmail(user.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("Registration successful. Please check your email for verification code.", true));
    }

    @Override
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequestDto request) {
        String refreshToken = request.refreshToken();
        TokenPair tokens = refreshTokenUseCase.refreshAccessToken(refreshToken);
        AuthResponseDto response = new AuthResponseDto(tokens.accessToken(), tokens.refreshToken());
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenRefreshRequestDto request) {
        refreshTokenUseCase.deleteRefreshTokenByToken(request.refreshToken());
        return ResponseEntity.ok(new ApiResponseDTO("Logout successful", true));
    }
}
