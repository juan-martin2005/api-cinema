package com.jg.sistema.cinema_room.infrastructure.controller;

import com.jg.sistema.cinema_room.application.port.in.GetSeatUseCase;
import com.jg.sistema.cinema_room.infrastructure.contract.ApiSeatDoc;
import com.jg.sistema.cinema_room.infrastructure.controller.dto.SeatResponseDto;
import com.jg.sistema.cinema_room.infrastructure.persistence.mapper.SeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/seats")
public class SeatController implements ApiSeatDoc {

    private final GetSeatUseCase getSeatUseCase;

    @Override
    @GetMapping("/room/{roomId}")
    public List<SeatResponseDto> getSeatsByCinemaRoom(@PathVariable UUID roomId) {
        return getSeatUseCase.getSeatsByCinemaRoom(roomId)
                .stream()
                .map(SeatMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @GetMapping("/{id}")
    public SeatResponseDto getSeatById(@PathVariable UUID id) {
        return SeatMapper.toResponse(getSeatUseCase.getSeatById(id));
    }
}
