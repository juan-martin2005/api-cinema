package com.jg.sistema.movie.infrastructure.persistence;

import com.jg.sistema.movie.application.port.out.MovieRepositoryPort;
import com.jg.sistema.movie.domain.model.Movie;
import com.jg.sistema.movie.infrastructure.persistence.mapper.MovieMapper;
import com.jg.sistema.movie.infrastructure.persistence.repositories.PostgreSqlMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaMovieRepository implements MovieRepositoryPort {

    private final PostgreSqlMovieRepository movieRepository;

    @Override
    @Cacheable(value = "movieSearch", key = "{#title, #genre}")
    public List<Movie> findAllMovies(String title, String genre) {
        return movieRepository.findAllWithFilter(title, genre).stream().map(MovieMapper::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existsByTitle(String title) {
        return movieRepository.existsByTitleIgnoreCase(title);
    }

    @Override
    @Cacheable(value = "movieById", key = "#id")
    public Optional<Movie> findById(UUID id) {
        return movieRepository.findById(id).map(MovieMapper::mapToDomain);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "movieById", key = "#result.id()", condition = "#movie.id() != null "),
            @CacheEvict(value = "movieSearch", allEntries = true),
    })
    public void save(Movie movie) {
        movieRepository.save(MovieMapper.mapToEntity(movie));
    }

}
