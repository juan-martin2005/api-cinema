package com.jg.sistema.movie.application.port.in;

import com.jg.sistema.movie.domain.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GetMovieUseCase {
    Page<Movie> search(Pageable pageable, String title, String genre);
    Movie getById(UUID id);
    List<Movie> getAllMoviesWithFilter(String title, String genre);
}
