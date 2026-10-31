package com.jg.sistema.movie.application.port.in;

import com.jg.sistema.movie.domain.model.Movie;
import org.springframework.web.multipart.MultipartFile;

public interface CreateMovieUseCase {
    void createMovie(Movie movie, MultipartFile file);
}
