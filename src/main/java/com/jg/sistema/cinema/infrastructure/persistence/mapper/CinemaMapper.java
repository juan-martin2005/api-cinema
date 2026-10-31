package com.jg.sistema.cinema.infrastructure.persistence.mapper;

import com.jg.sistema.cinema.domain.model.Cinema;
import com.jg.sistema.cinema.infrastructure.controller.dto.CinemaRequestDto;
import com.jg.sistema.cinema.infrastructure.controller.dto.CinemaResponseDto;
import com.jg.sistema.cinema.infrastructure.persistence.entities.CinemaEntity;

public class CinemaMapper {

    public static Cinema toDomain(CinemaEntity entity) {
        if (entity == null) return null;
        return new Cinema(
                entity.getId(),
                entity.getName(),
                entity.getImg(),
                entity.getAddress(),
                entity.getSecondAddress(),
                entity.getCity(),
                entity.getLatitude(),
                entity.getLongitude(),
                FormatMapper.toDomainSet(entity.getFormats()),
                entity.getRoomQuantity(),
                entity.getDescription()
        );
    }

    public static CinemaEntity toEntity(Cinema cinema) {
        if (cinema == null) return null;
        return CinemaEntity.builder()
                .id(cinema.id())
                .name(cinema.name())
                .img(cinema.img())
                .address(cinema.address())
                .secondAddress(cinema.secondAddress())
                .city(cinema.city())
                .latitude(cinema.latitude())
                .longitude(cinema.longitude())
                .formats(FormatMapper.toEntitySet(cinema.formats()))
                .roomQuantity(cinema.roomQuantity())
                .description(cinema.description())
                .build();
    }

    public static Cinema toDomain(CinemaRequestDto dto) {
        if (dto == null) return null;
        return new Cinema(
                null,
                dto.name(),
                dto.img(),
                dto.address(),
                dto.secondAddress(),
                dto.city(),
                dto.latitude(),
                dto.longitude(),
                null,
                dto.roomQuantity(),
                dto.description()
        );
    }

    public static CinemaResponseDto toResponse(Cinema cinema) {
        if (cinema == null) return null;
        return new CinemaResponseDto(
                cinema.id(),
                cinema.name(),
                cinema.img(),
                cinema.address(),
                cinema.secondAddress(),
                cinema.city(),
                cinema.latitude(),
                cinema.longitude(),
                FormatMapper.toResponseSet(cinema.formats()),
                cinema.roomQuantity(),
                cinema.description()
        );
    }
}
