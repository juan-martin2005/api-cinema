package com.jg.sistema.cinema_room.infrastructure.controller;

import com.jg.sistema.cinema_room.application.port.in.*;
import com.jg.sistema.cinema_room.domain.model.CinemaRoom;
import com.jg.sistema.cinema_room.domain.model.Seat;
import com.jg.sistema.cinema_room.infrastructure.contract.ApiCinemaRoomDoc;
import com.jg.sistema.cinema_room.infrastructure.controller.dto.CinemaRoomRequestDto;
import com.jg.sistema.cinema_room.infrastructure.controller.dto.CinemaRoomResponseDto;
import com.jg.sistema.cinema_room.infrastructure.persistence.mapper.CinemaRoomMapper;
import com.jg.sistema.cinema_room.infrastructure.persistence.mapper.SeatMapper;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cinema-rooms")
public class CinemaRoomController implements ApiCinemaRoomDoc {

    private final GetCinemaRoomUseCase getCinemaRoomUseCase;
    private final CreateCinemaRoomUseCase createCinemaRoomUseCase;
    private final UpdateCinemaRoomUseCase updateCinemaRoomUseCase;
    private final DeleteCinemaRoomUseCase deleteCinemaRoomUseCase;

    @Override
    @GetMapping
    public List<CinemaRoomResponseDto> getCinemaRooms() {
        return getCinemaRoomUseCase.getCinemaRooms().stream().map(CinemaRoomMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @GetMapping("/{id}")
    public CinemaRoomResponseDto getCinemaRoomById(@PathVariable UUID id) {
        return CinemaRoomMapper.toResponse(getCinemaRoomUseCase.getCinemaRoomById(id));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createCinemaRoom(@Valid @RequestBody CinemaRoomRequestDto cinemaRoomRequestDto) {
        CinemaRoom cinemaRoom = CinemaRoomMapper.toDomain(cinemaRoomRequestDto);
        List<Seat> seats = cinemaRoomRequestDto.seats().stream().map(SeatMapper::toDomain).collect(Collectors.toList());
        createCinemaRoomUseCase.createCinemaRoom(cinemaRoom, seats);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("Cinema Room created successfully", true));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateCinemaRoom(@PathVariable UUID id, @Valid @RequestBody CinemaRoomRequestDto cinemaRoomRequestDto) {
        CinemaRoom cinemaRoom = CinemaRoomMapper.toDomain(cinemaRoomRequestDto);
        updateCinemaRoomUseCase.updateCinemaRoom(id, cinemaRoom);
        return ResponseEntity.ok(new ApiResponseDTO("Cinema Room updated successfully", true));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteCinemaRoom(@PathVariable UUID id) {
        deleteCinemaRoomUseCase.deleteCinemaRoomById(id);
        return ResponseEntity.ok(new ApiResponseDTO("Cinema Room deleted successfully", true));
    }
}
