package com.jg.sistema.movie.infrastructure.persistence.mapper;

import com.jg.sistema.movie.domain.model.Movie;
import com.jg.sistema.movie.infrastructure.controller.dto.FunctionCinemaResponse;
import com.jg.sistema.movie.infrastructure.controller.dto.MovieRequestDto;
import com.jg.sistema.movie.infrastructure.controller.dto.MovieResponseDto;
import com.jg.sistema.movie.infrastructure.persistence.entities.MovieEntity;
import com.jg.sistema.show_time.domain.model.ShowTime;
import com.jg.sistema.show_time.infrastructure.controller.dto.ShowTimeResponseDto;
import com.jg.sistema.show_time.infrastructure.persistence.mapper.ShowTimeMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MovieMapper {

    public static Movie mapToDomain(MovieEntity movieEntity) {
        if (movieEntity == null) return null;
        return new Movie(
                movieEntity.getId(),
                movieEntity.getTitle(),
                movieEntity.getDescription(),
                movieEntity.getPosterUrl(),
                movieEntity.getClassification(),
                movieEntity.getDuration(),
                movieEntity.getGenre(),
                movieEntity.getIsNewRelease() != null && movieEntity.getIsNewRelease(),
                movieEntity.getIsPreSale() != null && movieEntity.getIsPreSale(),
                movieEntity.getOpeningDate()
        );
    }

    public static MovieEntity mapToEntity(Movie movie) {
        if (movie == null) return null;
        return MovieEntity.builder()
                .id(movie.id())
                .title(movie.title())
                .description(movie.description())
                .posterUrl(movie.posterUrl())
                .classification(movie.classification())
                .duration(movie.duration())
                .genre(movie.genre())
                .isNewRelease(movie.isNewRelease() != null && movie.isNewRelease())
                .isPreSale(movie.isPreSale() != null && movie.isPreSale())
                .openingDate(movie.openingDate())
                .build();
    }

    public static Movie mapToDomain(MovieRequestDto movieRequestDto) {
        if (movieRequestDto == null) return null;
        return new Movie(
                null,
                movieRequestDto.title(),
                movieRequestDto.description(),
                null,
                movieRequestDto.classification(),
                movieRequestDto.duration(),
                movieRequestDto.genre(),
                movieRequestDto.isNewRelease() != null && movieRequestDto.isNewRelease(),
                movieRequestDto.isPreSale() != null && movieRequestDto.isPreSale(),
                movieRequestDto.openingDate()
        );
    }

    public static MovieResponseDto mapToResponseDto(Movie movie, List<ShowTime> showTimes) {
        if (movie == null) return null;

        List<FunctionCinemaResponse> functionsCinema = functions(showTimes);

        return new MovieResponseDto(
                movie.id(),
                movie.title(),
                movie.description(),
                movie.posterUrl(),
                movie.classification(),
                movie.duration(),
                movie.genre(),
                functionsCinema,
                movie.isNewRelease() != null && movie.isNewRelease(),
                movie.isPreSale() != null && movie.isPreSale(),
                movie.openingDate()
        );
    }
    
    private static List<FunctionCinemaResponse> functions(List<ShowTime> showTimes){
        if (showTimes.isEmpty()) return Collections.emptyList();

        return showTimes.stream()
                .filter(st -> st.cinemaRoom().cinema() != null)

                .collect(Collectors.groupingBy(st -> st.cinemaRoom().cinema().id()))
                .keySet().stream()
                .map(
                        cinemaId -> new FunctionCinemaResponse(cinemaId, showTimes.stream()
                                .filter(st -> st.cinemaRoom().cinema().id().equals(cinemaId))
                                .map(ShowTimeMapper::toResponse)
                                .collect(Collectors.toList())
                        )
        ).collect(Collectors.toList());
    }
}
