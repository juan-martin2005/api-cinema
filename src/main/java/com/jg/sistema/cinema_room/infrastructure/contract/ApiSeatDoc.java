package com.jg.sistema.cinema_room.infrastructure.contract;

import com.jg.sistema.cinema_room.infrastructure.controller.dto.SeatResponseDto;
import com.jg.sistema.shared.infrastructure.controller.advice.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "Seats", description = "Endpoints for managing cinema seats")
public interface ApiSeatDoc {

    @Operation(summary = "Get seats by cinema room", description = "Retrieve all seats belonging to a specific cinema room",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of seats retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SeatResponseDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Cinema room not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    List<SeatResponseDto> getSeatsByCinemaRoom(UUID roomId);

    @Operation(summary = "Get seat by ID", description = "Retrieve a seat by its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Seat retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SeatResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Seat not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    SeatResponseDto getSeatById(UUID id);
}
