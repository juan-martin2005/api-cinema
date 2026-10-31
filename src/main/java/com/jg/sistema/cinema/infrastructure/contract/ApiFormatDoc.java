package com.jg.sistema.cinema.infrastructure.contract;

import com.jg.sistema.cinema.infrastructure.controller.dto.FormatRequestDto;
import com.jg.sistema.cinema.infrastructure.controller.dto.FormatResponseDto;
import com.jg.sistema.shared.infrastructure.controller.advice.ErrorResponse;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Formats", description = "Endpoints for managing movie/cinema formats (2D, 3D, IMAX, etc.) and prices")
public interface ApiFormatDoc {

    @Operation(summary = "Get all formats", description = "Retrieve a list of all available formats and their base prices",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of formats retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = FormatResponseDto.class))))
            })
    List<FormatResponseDto> getAllFormats();

    @Operation(summary = "Get format by ID", description = "Retrieve format details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Format found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FormatResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Format not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    FormatResponseDto getFormatById(Long id);

    @Operation(summary = "Create format", description = "Register a new format with name and base price",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Format created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Validation error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict - Format name already exists",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> createFormat(FormatRequestDto request);

    @Operation(summary = "Update format", description = "Update an existing format's name or price",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Format updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Format not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> updateFormat(Long id, FormatRequestDto request);

    @Operation(summary = "Delete format", description = "Delete a format by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Format deleted successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Format not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> deleteFormat(Long id);
}
