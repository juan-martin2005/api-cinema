package com.jg.sistema.movie.application.service;

import com.cloudinary.utils.ObjectUtils;
import com.jg.sistema.movie.application.port.in.CreateMovieUseCase;
import com.jg.sistema.movie.application.port.in.GetMovieUseCase;
import com.jg.sistema.movie.application.port.in.UpdateMovieUseCase;
import com.jg.sistema.movie.application.port.out.CloudStorePort;
import com.jg.sistema.movie.application.port.out.MovieRepositoryPort;
import com.jg.sistema.movie.domain.exception.EmptyFileException;
import com.jg.sistema.movie.domain.exception.InvalidTypeFileException;
import com.jg.sistema.movie.domain.exception.MovieAlreadyExistsException;
import com.jg.sistema.movie.domain.exception.MovieNotFoundException;
import com.jg.sistema.movie.domain.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieService implements GetMovieUseCase, CreateMovieUseCase, UpdateMovieUseCase {

    @Value("${movie.poster.directory}")
    private String DIRECTORY;
    private final MovieRepositoryPort movieRepository;
    private final CloudStorePort cloudStorePort;

    @Override
    @Transactional
    public List<Movie> getAllMoviesWithFilter(String title, String genre) {
        return movieRepository.findAllMovies(title, genre);
    }

    @Override
    @Transactional
    public Movie getById(UUID id) {
        return movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + id));
    }

    @Override
    @Transactional
    public void createMovie(Movie movie, MultipartFile file) {
        if(movieRepository.existsByTitle(movie.title())){
            throw new MovieAlreadyExistsException("Movie already exists with title: " + movie.title());
        }
        String posterUrl = saveFile(file);
        Movie cMovie = new Movie(
                movie.id(),
                movie.title(),
                movie.description(),
                posterUrl,
                movie.classification(),
                movie.duration(),
                movie.genre(),
                movie.isNewRelease(),
                movie.isPreSale(),
                movie.openingDate()
        );
        movieRepository.save(cMovie);
    }

    @Override
    @Transactional
    public void updateMovie(Movie movie, UUID id, MultipartFile file) {
        Movie m = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + id));
        String posterUrl = (file == null || file.isEmpty()) ? m.posterUrl() : saveFile(file);
        Movie uMovie = new Movie(
                id,
                movie.title(),
                movie.description(),
                posterUrl,
                movie.classification(),
                movie.duration(),
                movie.genre(),
                movie.isNewRelease(),
                movie.isPreSale(),
                movie.openingDate()
        );
        movieRepository.save(uMovie);
    }

    private String saveFile(MultipartFile file){
        if(file.isEmpty()){
            throw new EmptyFileException("File is empty");
        }
        try {
            String contentType = file.getContentType();
            validateContentType(contentType);

            //Parameter Cloudinary config
            Map<String, Object> params = ObjectUtils.asMap(
                    "folder", DIRECTORY,
                    "format","webp",
                    "quality","auto"
            );


            Map<?, ?> uploadResult = cloudStorePort.upload(file.getBytes(), params);
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to Cloudinary", e);
        }
    }

    private void validateContentType(String contentType){
        if(contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/webp"))){
            throw new InvalidTypeFileException("Invalid file type. Only JPEG, PNG and WEBP are allowed.");
        }
    }
}