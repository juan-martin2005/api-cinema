package com.jg.sistema.cinema_room.infrastructure.contract;

import com.jg.sistema.cinema_room.infrastructure.controller.dto.CinemaRoomRequestDto;
import com.jg.sistema.cinema_room.infrastructure.controller.dto.CinemaRoomResponseDto;
import com.jg.sistema.shared.infrastructure.controller.advice.ErrorResponse;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Cinema Room Management", description = "Endpoints for managing cinema room in the system")
@SecurityRequirement(name = "bearerAuth")
public interface ApiCinemaRoomDoc {

    @Operation(summary = "Get all cinema rooms", description = "Get all the system cinema rooms details, only accessible by admin users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    List<CinemaRoomResponseDto> getCinemaRooms();

    @Operation(summary = "Get cinema rooms by id", description = "Get cinema room details by id, only accessible by admin users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    CinemaRoomResponseDto getCinemaRoomById(UUID id);

    @Operation(summary = "Create cinema rooms", description = "Create a new cinema room in the system, only accessible by admin users",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successful created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict - Cinema Room with the same name already exists",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> createCinemaRoom(CinemaRoomRequestDto cinemaRoomRequestDto);

    @Operation(summary = "Update cinema rooms by id", description = "Update cinema room details by their unique identifier, only accessible by admin users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Not found - Cinema Room with the specified ID does not exist",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> updateCinemaRoom(UUID id, com.jg.sistema.cinema_room.infrastructure.controller.dto.CinemaRoomUpdateDto cinemaRoomRequestDto);

    @Operation(summary = "Delete cinema rooms by id", description = "Delete a cinema room from the system by their unique identifier, only accessible by admin users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful deleted",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Not found - Cinema Room with the specified ID does not exist",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> deleteCinemaRoom(UUID id);
}
