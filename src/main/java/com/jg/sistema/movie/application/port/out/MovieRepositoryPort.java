package com.jg.sistema.movie.application.port.out;

import com.jg.sistema.movie.domain.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieRepositoryPort {
    Page<Movie> search(Pageable pageable, String title, String genre);
    List<Movie> findAllMovies(String title, String genre);
    boolean existsByTitle(String title);
    Optional<Movie> findById(UUID id);
    Optional<Movie> findAuthoritativeById(UUID id);
    void save(Movie movie);

}
