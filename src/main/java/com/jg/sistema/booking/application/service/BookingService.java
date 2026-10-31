package com.jg.sistema.booking.application.service;

import com.jg.sistema.booking.application.port.in.CancelBookingUseCase;
import com.jg.sistema.booking.application.port.in.ConfirmBookingUseCase;
import com.jg.sistema.booking.application.port.in.CreateBookingUseCase;
import com.jg.sistema.booking.application.port.in.GetBookingUseCase;
import com.jg.sistema.booking.application.port.out.BookingRepositoryPort;
import com.jg.sistema.booking.domain.exception.BookingNotFoundException;
import com.jg.sistema.booking.domain.exception.InvalidBookingException;
import com.jg.sistema.booking.domain.exception.SeatAlreadyBookedException;
import com.jg.sistema.booking.domain.model.Booking;
import com.jg.sistema.booking.domain.model.BookingDetail;
import com.jg.sistema.booking.domain.model.BookingStatus;
import com.jg.sistema.cinema.application.port.out.FormatRepositoryPort;
import com.jg.sistema.cinema.domain.model.Format;
import com.jg.sistema.cinema_room.application.port.out.SeatRepositoryPort;
import com.jg.sistema.cinema_room.domain.exception.SeatNotFoundException;
import com.jg.sistema.cinema_room.domain.model.Seat;
import com.jg.sistema.show_time.application.port.out.ShowTimeRepositoryPort;
import com.jg.sistema.show_time.domain.exception.InvalidTimeException;
import com.jg.sistema.show_time.domain.exception.ShowTimeNotFoundException;
import com.jg.sistema.show_time.domain.model.ShowTime;
import com.jg.sistema.user.application.port.out.UserContextHolderPort;
import com.jg.sistema.user.application.port.out.UserRepositoryPort;
import com.jg.sistema.user.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService implements CreateBookingUseCase, GetBookingUseCase, CancelBookingUseCase, ConfirmBookingUseCase {

    private final BookingRepositoryPort bookingRepositoryPort;
    private final ShowTimeRepositoryPort showTimeRepositoryPort;
    private final SeatRepositoryPort seatRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final UserContextHolderPort userContextHolderPort;
    private final FormatRepositoryPort formatRepositoryPort;
    private final com.jg.sistema.shared.application.port.CoordinationPort coordination;
    private final com.jg.sistema.payments.application.port.PaymentOperations payments;
    private final java.time.Clock clock;
    @org.springframework.beans.factory.annotation.Value("${app.tax-rate:0.18}") private BigDecimal taxRate;


    @Transactional(readOnly = true)
    public Page<Booking> findPage(Pageable pageable) { return bookingRepositoryPort.findPage(pageable); }

    @Override
    @Transactional
    public Booking createBooking(UUID showTimeId, List<UUID> seatIds) {
        UUID userId = userContextHolderPort.getCurrentUserId();
        if (userId == null) {
            throw new UserNotFoundException("User is not authenticated");
        }
        userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        coordination.lockShowTime(showTimeId);
        ShowTime showTime = showTimeRepositoryPort.findShowTimeById(showTimeId)
                .orElseThrow(() -> new ShowTimeNotFoundException("ShowTime with id " + showTimeId + " not found"));

        if (showTime.startTime().isBefore(LocalDateTime.now(clock).plusMinutes(35))) {
            throw new InvalidTimeException("Bookings require at least 35 minutes before showtime");
        }

        if (seatIds == null || seatIds.isEmpty()) {
            throw new InvalidBookingException("At least one seat must be selected");
        }

        List<UUID> distinctSeatIds = seatIds.stream().distinct().toList();

        UUID roomId = showTime.cinemaRoom().id();
        List<Seat> roomSeats = seatRepositoryPort.findByCinemaRoomId(roomId);
        Map<UUID, Seat> seatMap = roomSeats.stream()
                .collect(Collectors.toMap(Seat::id, s -> s));

        List<Seat> selectedSeats = new ArrayList<>();
        for (UUID seatId : distinctSeatIds) {
            Seat seat = seatMap.get(seatId);
            if (seat == null) {
                throw new SeatNotFoundException("Seat with id " + seatId + " does not belong to room " + showTime.cinemaRoom().name());
            }
            selectedSeats.add(seat);
        }

        List<String> occupiedSeatLabels = bookingRepositoryPort.findOccupiedSeatLabelsByShowTimeId(showTimeId, LocalDateTime.now(clock));
        Set<String> occupiedSet = new HashSet<>(occupiedSeatLabels);

        List<String> conflictLabels = selectedSeats.stream()
                .map(Seat::label)
                .filter(occupiedSet::contains)
                .toList();

        if (!conflictLabels.isEmpty()) {
            throw new SeatAlreadyBookedException("Seat(s) " + conflictLabels + " are already booked for this showtime");
        }

        TicketPricing pricing = determineTicketPricing(showTime);

        BigDecimal seatCount = BigDecimal.valueOf(selectedSeats.size());
        BigDecimal subTotal = pricing.price().multiply(seatCount).setScale(2, RoundingMode.HALF_UP);
        BigDecimal tax = subTotal.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subTotal.add(tax).setScale(2, RoundingMode.HALF_UP);

        String code = generateUniqueBookingCode();

        List<BookingDetail> details = selectedSeats.stream()
                .map(seat -> new BookingDetail(
                        null,
                        null,
                        seat.label(),
                        pricing.ticketType(),
                        pricing.price()
                ))
                .collect(Collectors.toList());

        Booking booking = new Booking(
                null,
                code,
                showTimeId,
                userId,
                BookingStatus.PENDING,
                com.jg.sistema.booking.domain.model.PaymentStatus.UNPAID,
                subTotal,
                tax,
                total,
                LocalDateTime.now(clock),
                LocalDateTime.now(clock).plusMinutes(30),
                details
        );

        Booking saved = bookingRepositoryPort.save(booking);
        coordination.occupy(showTimeId, distinctSeatIds, saved.id());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        return bookingRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookings() {
        return bookingRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingById(UUID id) {
        Booking booking = bookingRepositoryPort.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id " + id + " not found"));
        authorize(booking.userId());
        return booking;
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingByCode(String code) {
        Booking booking = bookingRepositoryPort.findByCode(code)
                .orElseThrow(() -> new BookingNotFoundException("Booking with code " + code + " not found"));
        authorize(booking.userId());
        return booking;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getMyBookings() {
        UUID userId = userContextHolderPort.getCurrentUserId();
        if (userId == null) {
            throw new UserNotFoundException("User is not authenticated");
        }
        return bookingRepositoryPort.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByUserId(UUID userId) {
        authorize(userId);
        userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        return bookingRepositoryPort.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getOccupiedSeatsByShowTime(UUID showTimeId) {
        showTimeRepositoryPort.findShowTimeById(showTimeId)
                .orElseThrow(() -> new ShowTimeNotFoundException("ShowTime with id " + showTimeId + " not found"));
        return bookingRepositoryPort.findOccupiedSeatLabelsByShowTimeId(showTimeId, LocalDateTime.now(clock));
    }

    @Override
    public Booking cancelBooking(UUID id) { return payments.cancel(id); }

    @Override
    public Booking confirmBooking(UUID id) { return payments.reconcile(id); }

    private void authorize(UUID owner) {
        if (!userContextHolderPort.isAdmin() && !Objects.equals(owner, userContextHolderPort.getCurrentUserId()))
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Reservation belongs to another user");
    }

    private TicketPricing determineTicketPricing(ShowTime showTime) {
        if (showTime.format() == null || showTime.format().price() == null)
            throw new InvalidBookingException("Showtime has no configured format and price");
        return new TicketPricing(showTime.format().name(), showTime.format().price());
    }

    private String generateUniqueBookingCode() {
        String code;
        do {
            code = "BKG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (bookingRepositoryPort.existsByCode(code));
        return code;
    }

    private record TicketPricing(String ticketType, BigDecimal price) {}
}
