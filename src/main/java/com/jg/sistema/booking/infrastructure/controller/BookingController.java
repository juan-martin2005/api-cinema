package com.jg.sistema.booking.infrastructure.controller;

import com.jg.sistema.booking.application.port.in.CancelBookingUseCase;
import com.jg.sistema.booking.application.port.in.ConfirmBookingUseCase;
import com.jg.sistema.booking.application.port.in.CreateBookingUseCase;
import com.jg.sistema.booking.application.port.in.GetBookingUseCase;
import com.jg.sistema.booking.domain.model.Booking;
import com.jg.sistema.booking.infrastructure.contract.ApiBookingDoc;
import com.jg.sistema.booking.infrastructure.controller.dto.BookingRequestDto;
import com.jg.sistema.booking.infrastructure.controller.dto.BookingResponseDto;
import com.jg.sistema.booking.infrastructure.controller.dto.OccupiedSeatsResponseDto;
import com.jg.sistema.booking.infrastructure.persistence.mapper.BookingMapper;
import com.jg.sistema.shared.infrastructure.util.PaginationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController implements ApiBookingDoc {

    private final CreateBookingUseCase createBookingUseCase;
    private final GetBookingUseCase getBookingUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;
    private final ConfirmBookingUseCase confirmBookingUseCase;

    @Override
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingRequestDto request) {
        Booking booking = createBookingUseCase.createBooking(request.showTimeId(), request.seatIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(BookingMapper.toResponse(booking));
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<BookingResponseDto> getAllBookings() {
        return getBookingUseCase.getAllBookings().stream()
                .map(BookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @GetMapping("/paginated")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PagedModel<BookingResponseDto> getBookings(@RequestParam(defaultValue = "0", required = false) Integer page, @RequestParam(defaultValue = "15", required = false) Integer size) {

        if (page < 0 || size < 1 || size > 100) throw new IllegalArgumentException("Invalid page or size");
        Page<BookingResponseDto> bookings = getBookingUseCase.findPage(org.springframework.data.domain.PageRequest.of(page,size,org.springframework.data.domain.Sort.by("creatAt").descending())).map(BookingMapper::toResponse);
        return new PagedModel<>(bookings);
    }

    @Override
    @GetMapping("/my-bookings")
    @PreAuthorize("isAuthenticated()")
    public List<BookingResponseDto> getMyBookings() {
        return getBookingUseCase.getMyBookings().stream()
                .map(BookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public BookingResponseDto getBookingById(@PathVariable UUID id) {
        Booking booking = getBookingUseCase.getBookingById(id);
        return BookingMapper.toResponse(booking);
    }

    @Override
    @GetMapping("/code/{code}")
    @PreAuthorize("isAuthenticated()")
    public BookingResponseDto getBookingByCode(@PathVariable String code) {
        Booking booking = getBookingUseCase.getBookingByCode(code);
        return BookingMapper.toResponse(booking);
    }

    @Override
    @GetMapping("/showtime/{showTimeId}/occupied-seats")
    public OccupiedSeatsResponseDto getOccupiedSeats(@PathVariable UUID showTimeId) {
        List<String> occupied = getBookingUseCase.getOccupiedSeatsByShowTime(showTimeId);
        return new OccupiedSeatsResponseDto(showTimeId, occupied, occupied.size());
    }

    @Override
    @PutMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public BookingResponseDto cancelBooking(@PathVariable UUID id) {
        Booking booking = cancelBookingUseCase.cancelBooking(id);
        return BookingMapper.toResponse(booking);
    }

    @Override
    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAuthority('ADMIN')")
    public BookingResponseDto confirmBooking(@PathVariable UUID id) {
        Booking booking = confirmBookingUseCase.confirmBooking(id);
        return BookingMapper.toResponse(booking);
    }
}
