package com.jg.sistema.auth.infrastructure.contract;

import com.jg.sistema.shared.infrastructure.controller.advice.ErrorResponse;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Email Verification", description = "Endpoints for email verification and resending verification codes")
public interface ApiEmailDoc {
    @Operation(summary = "Email verification", description = "Verify email with the code sent to the user's email address",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "email"), @Parameter(in = ParameterIn.PATH, name = "code")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email successfully verified",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid email or code",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> verifyEmail(String email, String code);

    @Operation(summary = "Resend code", description = "Resend the email  verification code to the user's email",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "email")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "The verification code has been resent to the user's email",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid email",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> resendVerificationCode(@RequestParam String email);
}
