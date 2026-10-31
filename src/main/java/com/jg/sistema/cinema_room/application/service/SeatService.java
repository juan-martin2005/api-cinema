package com.jg.sistema.cinema_room.application.service;

import com.jg.sistema.cinema_room.application.port.in.GetSeatUseCase;
import com.jg.sistema.cinema_room.application.port.out.CinemaRoomRepositoryPort;
import com.jg.sistema.cinema_room.application.port.out.SeatRepositoryPort;
import com.jg.sistema.cinema_room.domain.exception.CinemaRoomNotFoundException;
import com.jg.sistema.cinema_room.domain.exception.SeatNotFoundException;
import com.jg.sistema.cinema_room.domain.model.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService implements GetSeatUseCase {

    private final SeatRepositoryPort seatRepositoryPort;
    private final CinemaRoomRepositoryPort cinemaRoomRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<Seat> getSeatsByCinemaRoom(UUID cinemaRoomId) {
        cinemaRoomRepositoryPort.findById(cinemaRoomId)
                .orElseThrow(() -> new CinemaRoomNotFoundException("Cinema room with id " + cinemaRoomId + " not found"));
        return seatRepositoryPort.findByCinemaRoomId(cinemaRoomId);
    }

    @Override
    @Transactional(readOnly = true)
    public Seat getSeatById(UUID seatId) {
        return seatRepositoryPort.findById(seatId)
                .orElseThrow(() -> new SeatNotFoundException("Seat with id " + seatId + " not found"));
    }
}
