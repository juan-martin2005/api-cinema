package com.jg.sistema.cinema.infrastructure.controller;

import com.jg.sistema.cinema.application.port.in.FormatUseCase;
import com.jg.sistema.cinema.domain.model.Format;
import com.jg.sistema.cinema.infrastructure.contract.ApiFormatDoc;
import com.jg.sistema.cinema.infrastructure.controller.dto.FormatRequestDto;
import com.jg.sistema.cinema.infrastructure.controller.dto.FormatResponseDto;
import com.jg.sistema.cinema.infrastructure.persistence.mapper.FormatMapper;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/formats")
public class FormatController implements ApiFormatDoc {

    private final FormatUseCase formatUseCase;

    @Override
    @GetMapping
    public List<FormatResponseDto> getAllFormats() {
        return formatUseCase.getAllFormats().stream()
                .map(FormatMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @GetMapping("/{id}")
    public FormatResponseDto getFormatById(@PathVariable Long id) {
        return FormatMapper.toResponse(formatUseCase.getFormatById(id));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createFormat(@Valid @RequestBody FormatRequestDto request) {
        Format format = FormatMapper.toDomain(request);
        formatUseCase.createFormat(format);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("Format created successfully", true));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateFormat(@PathVariable Long id, @Valid @RequestBody FormatRequestDto request) {
        Format format = FormatMapper.toDomain(request);
        formatUseCase.updateFormat(id, format);
        return ResponseEntity.ok(new ApiResponseDTO("Format updated successfully", true));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteFormat(@PathVariable Long id) {
        formatUseCase.deleteFormat(id);
        return ResponseEntity.ok(new ApiResponseDTO("Format deleted successfully", true));
    }
}
