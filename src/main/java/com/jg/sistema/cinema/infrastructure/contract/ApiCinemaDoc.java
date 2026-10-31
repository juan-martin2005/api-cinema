package com.jg.sistema.cinema.infrastructure.contract;

import com.jg.sistema.cinema.infrastructure.controller.dto.CinemaRequestDto;
import com.jg.sistema.cinema.infrastructure.controller.dto.CinemaResponseDto;
import com.jg.sistema.shared.infrastructure.controller.advice.ErrorResponse;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Cinemas", description = "Endpoints for managing cinemas/theaters, their locations and supported formats")
public interface ApiCinemaDoc {

    @Operation(summary = "Get all cinemas", description = "Retrieve a simple list of all cinema branches",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of cinemas retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CinemaResponseDto.class))))
            })
    List<CinemaResponseDto> getAllCinemas();

    @Operation(summary = "Get cinemas with filter and pagination", description = "Retrieve a paginated list of cinemas filtered by city and/or name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated cinemas retrieved successfully")
            })
    PagedModel<CinemaResponseDto> getCinemas(Pageable pageable, String city, String name);

    @Operation(summary = "Get cinema by ID", description = "Retrieve detailed information of a specific cinema branch",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cinema found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CinemaResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cinema not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    CinemaResponseDto getCinemaById(UUID id);

    @Operation(summary = "Create cinema", description = "Register a new cinema branch with address, formats, coordinates and description",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cinema created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Validation error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict - Cinema name already exists",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> createCinema(CinemaRequestDto request);

    @Operation(summary = "Update cinema", description = "Update an existing cinema's information, coordinates, or supported formats",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cinema updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Cinema not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> updateCinema(UUID id, CinemaRequestDto request);

    @Operation(summary = "Delete cinema", description = "Delete a cinema branch by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cinema deleted successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Cinema not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> deleteCinema(UUID id);
}
