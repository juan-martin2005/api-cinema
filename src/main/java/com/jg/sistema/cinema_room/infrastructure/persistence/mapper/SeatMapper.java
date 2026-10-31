package com.jg.sistema.cinema_room.infrastructure.persistence.mapper;

import com.jg.sistema.cinema_room.domain.model.Seat;
import com.jg.sistema.cinema_room.infrastructure.controller.dto.SeatRequestDto;
import com.jg.sistema.cinema_room.infrastructure.controller.dto.SeatResponseDto;
import com.jg.sistema.cinema_room.infrastructure.persistence.entities.CinemaRoomEntity;
import com.jg.sistema.cinema_room.infrastructure.persistence.entities.SeatEntity;

import java.util.List;
import java.util.stream.Collectors;

public class SeatMapper {

    public static List<SeatEntity> toListEntity(List<Seat> seat, CinemaRoomEntity roomEntity) {
        return seat.stream().map(s-> toEntity(s,roomEntity)).collect(Collectors.toList());
    }

    public static SeatEntity toEntity(Seat seat, CinemaRoomEntity roomEntity) {
        return SeatEntity.builder()
                .id(seat.id())
                .row(seat.row())
                .number(seat.number())
                .posX(seat.posX())
                .posY(seat.posY())
                .room(roomEntity)
                .build();
    }

    public static Seat toDomain(SeatEntity seat) {
        return new Seat(
                seat.getId(),
                seat.getNumber(),
                seat.getRow(),
                seat.getPosX(),
                seat.getPosY()
        );
    }

    public static Seat toDomain(SeatRequestDto seat) {
        return new Seat(
                null,
                seat.number(),
                seat.rowLetter(),
                seat.posX(),
                seat.posY()
        );
    }

    public static SeatResponseDto toResponse(Seat seat) {
        return new SeatResponseDto(
                seat.id(),
                seat.row(),
                seat.number(),
                seat.posX(),
                seat.posY()
        );
    }
}
