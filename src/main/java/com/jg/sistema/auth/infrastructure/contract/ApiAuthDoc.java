package com.jg.sistema.auth.infrastructure.contract;

import com.jg.sistema.auth.infrastructure.controller.dto.AuthRequestDTO;
import com.jg.sistema.auth.infrastructure.controller.dto.AuthResponseDto;
import com.jg.sistema.auth.infrastructure.controller.dto.TokenRefreshRequestDto;
import com.jg.sistema.shared.infrastructure.controller.advice.ErrorResponse;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import com.jg.sistema.user.infrastructure.controller.dto.UserRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication", description = "Endpoints for user authentication, registration, refresh token and logout")
public interface ApiAuthDoc {

    @Operation(summary = "User login", description = "Authenticate with email and password to receive an Access Token and a Refresh Token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful login",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid email or password, or user inactive/not verified",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> login(AuthRequestDTO loginRequest);

    @Operation(summary = "User register", description = "User registration: a code is sent to the email to verify the user's account",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successful registration",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid field input",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict - Email already in use",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> register(UserRequestDTO userRequestDTO);

    @Operation(summary = "Refresh access token", description = "Generates a new access token and a new rotated refresh token using a valid refresh token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tokens successfully renewed",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Refresh token expired",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Not found - Refresh token not found in database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> refreshToken(TokenRefreshRequestDto request);

    @Operation(summary = "User logout", description = "Invalidates and removes the provided refresh token from the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful logout",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class)))
            })
    ResponseEntity<?> logout(TokenRefreshRequestDto request);
}
