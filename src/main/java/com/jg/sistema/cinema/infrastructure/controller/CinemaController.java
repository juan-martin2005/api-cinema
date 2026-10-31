package com.jg.sistema.cinema.infrastructure.controller;

import com.jg.sistema.cinema.application.port.in.CreateCinemaUseCase;
import com.jg.sistema.cinema.application.port.in.DeleteCinemaUseCase;
import com.jg.sistema.cinema.application.port.in.GetCinemaUseCase;
import com.jg.sistema.cinema.application.port.in.UpdateCinemaUseCase;
import com.jg.sistema.cinema.domain.model.Cinema;
import com.jg.sistema.cinema.infrastructure.contract.ApiCinemaDoc;
import com.jg.sistema.cinema.infrastructure.controller.dto.CinemaRequestDto;
import com.jg.sistema.cinema.infrastructure.controller.dto.CinemaResponseDto;
import com.jg.sistema.cinema.infrastructure.persistence.mapper.CinemaMapper;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cinemas")
public class CinemaController implements ApiCinemaDoc {

    private final GetCinemaUseCase getCinemaUseCase;
    private final CreateCinemaUseCase createCinemaUseCase;
    private final UpdateCinemaUseCase updateCinemaUseCase;
    private final DeleteCinemaUseCase deleteCinemaUseCase;

    @Override
    @GetMapping
    public List<CinemaResponseDto> getAllCinemas() {
        return getCinemaUseCase.getAllCinemas().stream()
                .map(CinemaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @GetMapping("/filtered")
    public PagedModel<CinemaResponseDto> getCinemas(@PageableDefault(size = 15) Pageable pageable,
                                                    @RequestParam(required = false) String city,
                                                    @RequestParam(required = false) String name) {
        Page<CinemaResponseDto> cinemas = getCinemaUseCase.getCinemasWithFilter(pageable, city, name)
                .map(CinemaMapper::toResponse);
        return new PagedModel<>(cinemas);
    }

    @Override
    @GetMapping("/{id}")
    public CinemaResponseDto getCinemaById(@PathVariable UUID id) {
        Cinema cinema = getCinemaUseCase.getCinemaById(id);
        return CinemaMapper.toResponse(cinema);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createCinema(@Valid @RequestBody CinemaRequestDto request) {
        Cinema cinema = CinemaMapper.toDomain(request);
        createCinemaUseCase.createCinema(cinema, request.formatIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("Cinema created successfully", true));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateCinema(@PathVariable UUID id, @Valid @RequestBody CinemaRequestDto request) {
        Cinema cinema = CinemaMapper.toDomain(request);
        updateCinemaUseCase.updateCinema(id, cinema, request.formatIds());
        return ResponseEntity.ok(new ApiResponseDTO("Cinema updated successfully", true));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteCinema(@PathVariable UUID id) {
        deleteCinemaUseCase.deleteCinema(id);
        return ResponseEntity.ok(new ApiResponseDTO("Cinema deleted successfully", true));
    }
}
