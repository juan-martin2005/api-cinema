package com.jg.sistema.cinema.infrastructure.persistence.mapper;

import com.jg.sistema.cinema.domain.model.Format;
import com.jg.sistema.cinema.infrastructure.controller.dto.FormatRequestDto;
import com.jg.sistema.cinema.infrastructure.controller.dto.FormatResponseDto;
import com.jg.sistema.cinema.infrastructure.persistence.entities.FormatEntity;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class FormatMapper {

    public static Format toDomain(FormatEntity entity) {
        if (entity == null) return null;
        return new Format(
                entity.getId(),
                entity.getName(),
                entity.getPrice()
        );
    }

    public static FormatEntity toEntity(Format format) {
        if (format == null) return null;
        return FormatEntity.builder()
                .id(format.id())
                .name(format.name())
                .price(format.price())
                .build();
    }

    public static Format toDomain(FormatRequestDto dto) {
        if (dto == null) return null;
        return new Format(
                null,
                dto.name(),
                dto.price()
        );
    }

    public static FormatResponseDto toResponse(Format format) {
        if (format == null) return null;
        return new FormatResponseDto(
                format.id(),
                format.name(),
                format.price()
        );
    }

    public static Set<Format> toDomainSet(Set<FormatEntity> entities) {
        if (entities == null) return Collections.emptySet();
        return entities.stream().map(FormatMapper::toDomain).collect(Collectors.toSet());
    }

    public static Set<FormatEntity> toEntitySet(Set<Format> formats) {
        if (formats == null) return Collections.emptySet();
        return formats.stream().map(FormatMapper::toEntity).collect(Collectors.toSet());
    }

    public static Set<FormatResponseDto> toResponseSet(Set<Format> formats) {
        if (formats == null) return Collections.emptySet();
        return formats.stream().map(FormatMapper::toResponse).collect(Collectors.toSet());
    }
}
