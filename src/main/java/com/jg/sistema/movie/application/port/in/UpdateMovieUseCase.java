package com.jg.sistema.movie.application.port.in;

import com.jg.sistema.movie.domain.model.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface UpdateMovieUseCase {
    void updateMovie(Movie movie, UUID id, MultipartFile file);
}
