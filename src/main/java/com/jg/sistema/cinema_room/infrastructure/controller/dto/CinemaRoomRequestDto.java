package com.jg.sistema.cinema_room.infrastructure.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CinemaRoomRequestDto(
        @NotNull(message = "Cinema ID is required")
        UUID cinemaId,

        @NotBlank(message = "Room name is required")
        String name,

        @NotEmpty(message = "List of seat is required")
        @Valid List<@Valid SeatRequestDto> seats

) {
}
