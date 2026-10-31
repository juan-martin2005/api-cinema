package com.jg.sistema.booking.infrastructure.contract;

import com.jg.sistema.booking.infrastructure.controller.dto.BookingRequestDto;
import com.jg.sistema.booking.infrastructure.controller.dto.BookingResponseDto;
import com.jg.sistema.booking.infrastructure.controller.dto.OccupiedSeatsResponseDto;
import com.jg.sistema.shared.infrastructure.controller.advice.ErrorResponse;
import com.jg.sistema.shared.infrastructure.controller.dto.ApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Bookings", description = "Endpoints for managing ticket bookings and reservations")
public interface ApiBookingDoc {

    @Operation(summary = "Create a booking", description = "Create a new booking by selecting a showtime and a list of seats. Calculates totals and assigns ticket details.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Booking created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookingResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Validation error or invalid seats",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Showtime, room or seat not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Seat(s) already booked for this showtime",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<BookingResponseDto> createBooking(BookingRequestDto request);

    @Operation(summary = "Get all bookings (Admin)", description = "Retrieve a list of all bookings in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of bookings retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookingResponseDto.class))))
            })
    List<BookingResponseDto> getAllBookings();

    @Operation(summary = "Get paginated bookings (Admin)", description = "Retrieve paginated bookings in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated bookings retrieved successfully")
            })
    PagedModel<BookingResponseDto> getBookings(Integer page, Integer size);

    @Operation(summary = "Get current user bookings", description = "Retrieve all bookings belonging to the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User bookings retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookingResponseDto.class))))
            })
    List<BookingResponseDto> getMyBookings();

    @Operation(summary = "Get booking by ID", description = "Retrieve detailed information of a booking by its UUID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Booking not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    BookingResponseDto getBookingById(UUID id);

    @Operation(summary = "Get booking by code", description = "Retrieve a booking by its unique booking code (e.g., BKG-ABCD1234)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Booking not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    BookingResponseDto getBookingByCode(String code);

    @Operation(summary = "Get occupied seats for a showtime", description = "Retrieve list of seat labels (e.g. A1, B4) already occupied/reserved for a specific showtime",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Occupied seats retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OccupiedSeatsResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Showtime not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    OccupiedSeatsResponseDto getOccupiedSeats(UUID showTimeId);

    @Operation(summary = "Cancel a booking", description = "Cancel an existing booking and mark its status as CANCELLED",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking cancelled successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookingResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Booking already cancelled",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Booking not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    BookingResponseDto cancelBooking(UUID id);

    @Operation(summary = "Confirm a booking", description = "Confirm a pending booking and mark payment as complete",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking confirmed successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Booking not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    BookingResponseDto confirmBooking(UUID id);
}
