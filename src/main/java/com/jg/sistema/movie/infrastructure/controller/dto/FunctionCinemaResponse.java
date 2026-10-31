package com.jg.sistema.movie.infrastructure.controller.dto;

import com.jg.sistema.show_time.infrastructure.controller.dto.ShowTimeResponseDto;

import java.util.List;
import java.util.UUID;

public record FunctionCinemaResponse(
        UUID cinemaId,
        List<ShowTimeResponseDto> functions
) {
}
