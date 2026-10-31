package com.jg.sistema.movie.infrastructure.contract;

import com.jg.sistema.movie.infrastructure.controller.dto.MovieRequestDto;
import com.jg.sistema.movie.infrastructure.controller.dto.MovieResponseDto;
import com.jg.sistema.shared.infrastructure.controller.advice.ErrorResponse;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Movie Management", description = "Endpoints for managing movies in the system")
@SecurityRequirement(name = "bearerAuth")
public interface ApiMovieDoc {

    @Operation(summary = "Get all movies", description = "Get all the system movie details, only accessible by admin users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    PagedModel<MovieResponseDto> getAllMovies(Pageable pageable, String title, String genre);

    @Operation(summary = "Get movie by id", description = "Get movie details by their unique identifier, only accessible by admin users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Movie not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    MovieResponseDto getMovieById(UUID id);

    @Operation(summary = "Create movie", description = "Movie is created by an admin user, the request must include a multipart file for the movie poster" +
            " and a JSON body with the movie details. The movie will be stored in the database and the poster will be saved in the file system.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successful created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid field input",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict - Movie already in use",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            },
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            encoding = @Encoding(
                                    name = "movieRequest",
                                    contentType = MediaType.APPLICATION_JSON_VALUE
                            )
                    )
            )
    )
    ResponseEntity<?> createMovie(MultipartFile file, MovieRequestDto movieRequest);

    @Operation(summary = "Update movie", description = "Movie is updated by an admin user, the request must include a multipart file for the movie poster " +
            "and a JSON body with the movie details. The movie will be updated in the database and the poster will be replaced in the file system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid field input",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict - Movie already in use",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or does not have permission",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<?> updateMovie(UUID id, MultipartFile file, MovieRequestDto movieRequest);

}
