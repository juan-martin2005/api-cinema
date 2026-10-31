package com.jg.sistema.show_time.infrastructure.controller;

import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import com.jg.sistema.show_time.application.port.in.CreateShowTimeUseCase;
import com.jg.sistema.show_time.application.port.in.GetShowTimeUseCase;
import com.jg.sistema.show_time.application.port.in.UpdateShowTimeUseCase;
import com.jg.sistema.show_time.infrastructure.controller.dto.ShowTimeRequestDto;
import com.jg.sistema.show_time.infrastructure.controller.dto.ShowTimeResponseDto;
import com.jg.sistema.show_time.infrastructure.persistence.mapper.ShowTimeMapper;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/show-times")
public class ShowTimeController {

    private final GetShowTimeUseCase getShowTimeUseCase;
    private final CreateShowTimeUseCase createShowTimeUseCase;
    private final UpdateShowTimeUseCase updateShowTimeUseCase;

    @GetMapping
    public List<ShowTimeResponseDto> getAllShowTimes() {
        return getShowTimeUseCase.getAllShowTimes().stream().map(ShowTimeMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/movie")
    public List<ShowTimeResponseDto> getAllShowTimesByMovie(@RequestParam UUID movieId) {
        return getShowTimeUseCase.getShowTimeByMovieIdAndStartTimeAfter(movieId, LocalDateTime.now()).stream().map(ShowTimeMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ShowTimeResponseDto getShowTimeById(@PathVariable UUID id) {
        return ShowTimeMapper.toResponse(getShowTimeUseCase.getShowTimedById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createShowTime(@Valid @RequestBody ShowTimeRequestDto req) {
        createShowTimeUseCase.createShowTime(ShowTimeMapper.toDomain(req));
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("Show time created successfully", true));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateShowTime(@PathVariable UUID id, @Valid @RequestBody ShowTimeRequestDto req) {
        updateShowTimeUseCase.updateShowTime(id, ShowTimeMapper.toDomain(req));
        return ResponseEntity.ok(new ApiResponseDTO("Show time updated successfully", true));
    }
}
