package com.jg.sistema.user.infrastructure.contract;

import com.jg.sistema.shared.infrastructure.controller.advice.ErrorResponse;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import com.jg.sistema.user.infrastructure.controller.dto.ChangeUserPasswordDto;
import com.jg.sistema.user.infrastructure.controller.dto.UserAdminRequestDTO;
import com.jg.sistema.user.infrastructure.controller.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "User Management", description = "Endpoints for managing users in the system")
@SecurityRequirement(name = "bearerAuth")
public interface ApiUserDoc {
    @Operation(summary = "Get all user", description = "Get all the system user details, only accessible by admin users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - User not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    List<UserResponseDTO> getAllUsers();

    @Operation(summary = "Get user by id", description = "Get user details by their unique identifier, only accessible by admin users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - User not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    UserResponseDTO getUserById(UUID id);

    @Operation(summary = "Create user", description = "User is created by an admin, with the different roles of the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successful created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid field input",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict - Email already in use",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> createUser(UserAdminRequestDTO userRequest);

    @Operation(summary = "Change the user status", description = "Change de user status to active or inactive, this is useful when you want to disable a user without deleting it",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful status change",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - User not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))

            })
    ResponseEntity<?> toggleUserStatus(UUID id);

    @Operation(summary = "Change password", description = "Change de user password, this endpoint is used by the user to change his password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful password change",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid password input",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))

            })
    ResponseEntity<?> changeUserPassword(ChangeUserPasswordDto changePassword);
}
