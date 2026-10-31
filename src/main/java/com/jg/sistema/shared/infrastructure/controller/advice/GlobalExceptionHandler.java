package com.jg.sistema.shared.infrastructure.controller.advice;

import com.jg.sistema.auth.domain.exception.*;
import com.jg.sistema.booking.domain.exception.BookingNotFoundException;
import com.jg.sistema.booking.domain.exception.InvalidBookingException;
import com.jg.sistema.booking.domain.exception.SeatAlreadyBookedException;
import com.jg.sistema.cinema.domain.exception.CinemaAlreadyExistsException;
import com.jg.sistema.cinema.domain.exception.CinemaNotFoundException;
import com.jg.sistema.cinema.domain.exception.FormatAlreadyExistsException;
import com.jg.sistema.cinema.domain.exception.FormatNotFoundException;
import com.jg.sistema.cinema_room.domain.exception.CinemaRoomAlreadyExistsException;
import com.jg.sistema.cinema_room.domain.exception.CinemaRoomCapacityException;
import com.jg.sistema.cinema_room.domain.exception.CinemaRoomNotFoundException;
import com.jg.sistema.cinema_room.domain.exception.SeatNotFoundException;
import com.jg.sistema.movie.domain.exception.EmptyFileException;
import com.jg.sistema.movie.domain.exception.InvalidTypeFileException;
import com.jg.sistema.movie.domain.exception.MovieAlreadyExistsException;
import com.jg.sistema.movie.domain.exception.MovieNotFoundException;
import com.jg.sistema.show_time.domain.exception.InvalidTimeException;
import com.jg.sistema.show_time.domain.exception.ShowTimeAlreadyExistException;
import com.jg.sistema.show_time.domain.exception.ShowTimeNotFoundException;
import com.jg.sistema.user.domain.exception.PasswordArgumentException;
import com.jg.sistema.user.domain.exception.RolNotFoundException;
import com.jg.sistema.user.domain.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Data Integrity Violation",
                "The referenced entity cannot be deleted because it is associated with other entities. Please remove the associations before deleting.",
                409
        );
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentExceptions(MethodArgumentNotValidException ex) {
        Map<Object, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        ErrorResponse errorResponse = new ErrorResponse(
                "Error in fields",
                errors,
                400
        );
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "User Not Found",
                ex.getMessage(),
                404
        );
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(VerifyCodeException.class)
    public ResponseEntity<?> handleVerifyCodeException(VerifyCodeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid Verification Code",
                ex.getMessage(),
                400
        );
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(RolNotFoundException.class)
    public ResponseEntity<?> handleRolNotFoundException(RolNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Role Not Found",
                ex.getMessage(),
                404
        );
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailConflictException(EmailAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Email Conflict",
                ex.getMessage(),
                409
        );
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<?> handleInvalidCredentialException(InvalidCredentialException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid Credentials",
                ex.getMessage(),
                401
        );
        return ResponseEntity.status(401).body(errorResponse);
    }

//    @ExceptionHandler(InvalidTokenException.class)
//    public ResponseEntity<?> handleInvalidTokenException(InvalidTokenException ex) {
//        ErrorResponse errorResponse = new ErrorResponse(
//                "Invalid Token",
//                ex.getMessage(),
//                401
//        );
//        return ResponseEntity.status(401).body(errorResponse);
//    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Forbidden",
                ex.getMessage(),
                403
        );
        return ResponseEntity.status(403).body(errorResponse);
    }

    @ExceptionHandler (MovieNotFoundException.class)
    public ResponseEntity<?> handleMovieNotFoundException(MovieNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Movie Not Found",
                ex.getMessage(),
                404
        );
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler (MovieAlreadyExistsException.class)
    public ResponseEntity<?> handleMovieAlreadyExistsException(MovieAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Movie Already Exists",
                ex.getMessage(),
                409
        );
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler({CinemaRoomAlreadyExistsException.class, CinemaRoomNotFoundException.class, CinemaRoomCapacityException.class})
    public ResponseEntity<?> handleCinemaRoomAlreadyExistsException(Exception ex) {
        if(ex instanceof CinemaRoomAlreadyExistsException) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Cinema Room Already Exists",
                    ex.getMessage(),
                    409
            );
            return ResponseEntity.status(409).body(errorResponse);
        }
        if(ex instanceof CinemaRoomNotFoundException) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Cinema Room Not Found",
                    ex.getMessage(),
                    404
            );
            return ResponseEntity.status(404).body(errorResponse);
        }

        ErrorResponse errorResponse = new ErrorResponse(
                "Cinema Room Capacity error",
                ex.getMessage(),
                400
        );
        return ResponseEntity.status(400).body(errorResponse);

    }

    @ExceptionHandler(SeatNotFoundException.class)
    public ResponseEntity<?> handleSeatNotFoundException(SeatNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Seat Not Found",
                ex.getMessage(),
                404
        );
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler({CinemaNotFoundException.class, FormatNotFoundException.class})
    public ResponseEntity<?> handleCinemaAndFormatNotFoundException(RuntimeException ex) {
        String title = ex instanceof CinemaNotFoundException ? "Cinema Not Found" : "Format Not Found";
        ErrorResponse errorResponse = new ErrorResponse(
                title,
                ex.getMessage(),
                404
        );
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler({CinemaAlreadyExistsException.class, FormatAlreadyExistsException.class})
    public ResponseEntity<?> handleCinemaAndFormatAlreadyExistsException(RuntimeException ex) {
        String title = ex instanceof CinemaAlreadyExistsException ? "Cinema Already Exists" : "Format Already Exists";
        ErrorResponse errorResponse = new ErrorResponse(
                title,
                ex.getMessage(),
                409
        );
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler(PasswordArgumentException.class)
    public ResponseEntity<?> handleCinemaRoomNotFoundException(PasswordArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid Password Argument",
                ex.getMessage(),
                400
        );
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler({InvalidTypeFileException.class, EmptyFileException.class})
    public ResponseEntity<?> handleFileExceptions(Exception ex) {
        if(ex instanceof InvalidTypeFileException) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Invalid File Type",
                    ex.getMessage(),
                    400
            );
            return ResponseEntity.status(400).body(errorResponse);
        }

        ErrorResponse errorResponse = new ErrorResponse(
                "Empty File",
                ex.getMessage(),
                400
        );
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler({ShowTimeNotFoundException.class, InvalidTimeException.class, ShowTimeAlreadyExistException.class})
    public ResponseEntity<?> handleInvalidTimeException(Exception ex) {
        if(ex instanceof ShowTimeNotFoundException) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "ShowTime Not Found",
                    ex.getMessage(),
                    404
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
        if(ex instanceof InvalidTimeException) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Invalid Time",
                    ex.getMessage(),
                    400
            );
            return ResponseEntity.status(400).body(errorResponse);
        }

        ErrorResponse errorResponse = new ErrorResponse(
                "ShowTime Already Exists",
                ex.getMessage(),
                409
        );
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler({RefreshTokenExpiredException.class, RefreshTokenNotFoundException.class})
    public ResponseEntity<?> handleInvalidTokensException(Exception ex) {
        if(ex instanceof RefreshTokenExpiredException) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Refresh Token has expired",
                    ex.getMessage(),
                    401
            );
            return ResponseEntity.status(401).body(errorResponse);
        }
        ErrorResponse errorResponse = new ErrorResponse(
                "Refresh Token not found",
                ex.getMessage(),
                404
        );
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Endpoint Not Found",
                "The requested route '/" + ex.getResourcePath() + "' does not exist.",
                404
        );
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<?> handleBookingNotFoundException(BookingNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Booking Not Found",
                ex.getMessage(),
                404
        );
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(SeatAlreadyBookedException.class)
    public ResponseEntity<?> handleSeatAlreadyBookedException(SeatAlreadyBookedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Seat Already Booked",
                ex.getMessage(),
                409
        );
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler(InvalidBookingException.class)
    public ResponseEntity<?> handleInvalidBookingException(InvalidBookingException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid Booking Request",
                ex.getMessage(),
                400
        );
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Method Not Allowed",
                "HTTP method '" + ex.getMethod() + "' is not supported for this endpoint.",
                405
        );
        return ResponseEntity.status(405).body(errorResponse);
    }
}
