package com.jg.sistema.show_time.infrastructure.persistence.mapper;

import com.jg.sistema.cinema_room.domain.model.CinemaRoom;
import com.jg.sistema.cinema_room.infrastructure.persistence.mapper.CinemaRoomMapper;
import com.jg.sistema.movie.domain.model.Movie;
import com.jg.sistema.movie.infrastructure.persistence.mapper.MovieMapper;
import com.jg.sistema.show_time.domain.model.ShowTime;
import com.jg.sistema.show_time.infrastructure.controller.dto.ShowTimeRequestDto;
import com.jg.sistema.show_time.infrastructure.controller.dto.ShowTimeResponseDto;
import com.jg.sistema.show_time.infrastructure.persistence.entities.ShowTimeEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowTimeMapper {

    public static ShowTime toDomain(ShowTimeEntity entity) {
        if (entity == null) return null;
        return new ShowTime(
                entity.getId(),
                MovieMapper.mapToDomain(entity.getMovie()),
                CinemaRoomMapper.toDomain(entity.getCinemaRoom()),
                entity.getStartTime(),
                entity.getEndTime()
        );
    }

    public static ShowTimeEntity toEntity(ShowTime showTime) {
        if (showTime == null) return null;
        return ShowTimeEntity.builder()
                .id(showTime.id())
                .movie(MovieMapper.mapToEntity(showTime.movie()))
                .cinemaRoom(CinemaRoomMapper.toEntity(showTime.cinemaRoom()))
                .startTime(showTime.startTime())
                .endTime(showTime.endTime())
                .build();
    }

    public static ShowTime toDomain(ShowTimeRequestDto req) {
        if (req == null) return null;
        return new ShowTime(
                null,
                Movie.withId(req.movieId()),
                CinemaRoom.withId(req.cinemaRoomId()),
                req.startTime(),
                null
        );
    }

    public static ShowTimeResponseDto toResponse(ShowTime showTime) {
        if (showTime == null) return null;
        return new ShowTimeResponseDto(
                showTime.id(),
                CinemaRoomMapper.toResponse(showTime.cinemaRoom()),
                showTime.startTime()
        );
    }
}
