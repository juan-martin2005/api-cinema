package com.jg.sistema.cinema_room.infrastructure.persistence.mapper;

import com.jg.sistema.cinema.domain.model.Cinema;
import com.jg.sistema.cinema.domain.model.Format;
import com.jg.sistema.cinema.infrastructure.persistence.mapper.CinemaMapper;
import com.jg.sistema.cinema_room.domain.model.CinemaRoom;
import com.jg.sistema.cinema_room.infrastructure.controller.dto.CinemaRoomRequestDto;
import com.jg.sistema.cinema_room.infrastructure.controller.dto.CinemaRoomResponseDto;
import com.jg.sistema.cinema_room.infrastructure.persistence.entities.CinemaRoomEntity;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CinemaRoomMapper {

    public static CinemaRoomEntity toEntity(CinemaRoom cinemaRoom) {
        if (cinemaRoom == null) return null;
        return CinemaRoomEntity.builder()
                .id(cinemaRoom.id())
                .name(cinemaRoom.name())
                .capacity(cinemaRoom.capacity())
                .cinema(cinemaRoom.cinema() != null ? CinemaMapper.toEntity(cinemaRoom.cinema()) : null)
                .build();
    }

    public static CinemaRoom toDomain(CinemaRoomEntity entity) {
        if (entity == null) return null;
        Cinema cinema = entity.getCinema() != null ? CinemaMapper.toDomain(entity.getCinema()) : null;

        return new CinemaRoom(
                entity.getId(),
                entity.getName(),
                entity.getCapacity(),
                null,
                cinema
        );
    }

    public static CinemaRoomResponseDto toResponse(CinemaRoom cinemaRoom) {
        if (cinemaRoom == null) return null;

        UUID cinemaId = cinemaRoom.cinema() != null ? cinemaRoom.cinema().id() : null;
        Set<String> formats = (cinemaRoom.cinema() != null && cinemaRoom.cinema().formats() != null)
                ? cinemaRoom.cinema().formats().stream()
                        .map(Format::name)
                        .collect(Collectors.toSet())
                : Collections.emptySet();

        Set<String> languages = Collections.emptySet();

        return new CinemaRoomResponseDto(
                cinemaRoom.id(),
                cinemaRoom.name(),
                cinemaId,
                formats,
                languages
        );
    }

    public static CinemaRoom toDomain(CinemaRoomRequestDto dto) {
        if (dto == null) return null;
        return new CinemaRoom(
                null,
                dto.name(),
                dto.seats().size(),
                dto.seats().stream().map(SeatMapper::toDomain).collect(Collectors.toList()),
                dto.cinemaId() != null ? Cinema.withId(dto.cinemaId()) : null
        );
    }
}
