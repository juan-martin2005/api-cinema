package com.jg.sistema.movie.infrastructure.controller;

import com.jg.sistema.movie.domain.model.Movie;
import com.jg.sistema.movie.infrastructure.contract.ApiMovieDoc;
import com.jg.sistema.movie.infrastructure.persistence.mapper.MovieMapper;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import com.jg.sistema.movie.application.port.in.CreateMovieUseCase;
import com.jg.sistema.movie.application.port.in.GetMovieUseCase;
import com.jg.sistema.movie.application.port.in.UpdateMovieUseCase;
import com.jg.sistema.movie.infrastructure.controller.dto.MovieRequestDto;
import com.jg.sistema.movie.infrastructure.controller.dto.MovieResponseDto;
import com.jg.sistema.show_time.application.port.in.GetShowTimeUseCase;
import com.jg.sistema.show_time.domain.model.ShowTime;
import com.jg.sistema.shared.infrastructure.util.PaginationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/movies")
public class MovieController implements ApiMovieDoc {
    private final java.time.Clock clock;

    private final GetMovieUseCase getMovieUseCase;
    private final CreateMovieUseCase createMovieUseCase;
    private final UpdateMovieUseCase updateMovieUseCase;
    private final GetShowTimeUseCase getShowTimeUseCase;

    @GetMapping()
    public PagedModel<MovieResponseDto> getAllMovies(@PageableDefault(size = 20) Pageable pageable,
                                                     @RequestParam(required = false) String title,
                                                     @RequestParam(required = false)  String genre) {

        Page<Movie> moviesPage = getMovieUseCase.search(pageable, title, genre);

        List<UUID> movieIds = moviesPage.getContent().stream().map(Movie::id).collect(Collectors.toList());

        Map<UUID, List<ShowTime>> showTimesByMovieId = movieIds.isEmpty()
                ? Collections.emptyMap()
                : getShowTimeUseCase.getShowTimesByMovieIdsAndStartTimeAfter(movieIds, LocalDateTime.now(clock))
                        .stream()
                        .collect(Collectors.groupingBy(st -> st.movie().id()));

        Page<MovieResponseDto> movies = moviesPage.map(movie -> {
            List<ShowTime> functions = showTimesByMovieId.getOrDefault(movie.id(), Collections.emptyList());
            return MovieMapper.mapToResponseDto(movie, functions);
        });

        return new PagedModel<>(movies);
    }

    @GetMapping("/{id}")
    public MovieResponseDto getMovieById(@PathVariable UUID id) {
        Movie movie = getMovieUseCase.getById(id);
        List<ShowTime> functions = getShowTimeUseCase.getShowTimeByMovieIdAndStartTimeAfter(movie.id(), LocalDateTime.now(clock));
        return MovieMapper.mapToResponseDto(movie, functions);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createMovie(@RequestPart("file") MultipartFile file,
                                         @Valid @RequestPart MovieRequestDto movieRequest) {
        createMovieUseCase.createMovie(MovieMapper.mapToDomain(movieRequest), file);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("Movie created successfully",true));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMovie(@PathVariable UUID id,
                                         @RequestPart(value = "file", required = false) MultipartFile file,
                                         @Valid @RequestPart MovieRequestDto movieRequest) {
        updateMovieUseCase.updateMovie(MovieMapper.mapToDomain(movieRequest), id, file);
        return ResponseEntity.ok(new ApiResponseDTO("Movie updated successfully",true));
    }

}
