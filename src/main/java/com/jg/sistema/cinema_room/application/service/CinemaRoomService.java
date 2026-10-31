package com.jg.sistema.cinema_room.application.service;

import com.jg.sistema.cinema.application.port.out.CinemaRepositoryPort;
import com.jg.sistema.cinema.domain.exception.CinemaNotFoundException;
import com.jg.sistema.cinema.domain.model.Cinema;
import com.jg.sistema.cinema_room.application.port.in.CreateCinemaRoomUseCase;
import com.jg.sistema.cinema_room.application.port.in.DeleteCinemaRoomUseCase;
import com.jg.sistema.cinema_room.application.port.in.GetCinemaRoomUseCase;
import com.jg.sistema.cinema_room.application.port.in.UpdateCinemaRoomUseCase;
import com.jg.sistema.cinema_room.application.port.out.CinemaRoomRepositoryPort;
import com.jg.sistema.cinema_room.application.port.out.SeatRepositoryPort;
import com.jg.sistema.cinema_room.domain.exception.CinemaRoomAlreadyExistsException;
import com.jg.sistema.cinema_room.domain.exception.CinemaRoomCapacityException;
import com.jg.sistema.cinema_room.domain.exception.CinemaRoomNotFoundException;
import com.jg.sistema.cinema_room.domain.model.CinemaRoom;
import com.jg.sistema.cinema_room.domain.model.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaRoomService implements GetCinemaRoomUseCase, CreateCinemaRoomUseCase, UpdateCinemaRoomUseCase, DeleteCinemaRoomUseCase {

    private final CinemaRoomRepositoryPort cinemaRoomRepository;
    private final SeatRepositoryPort seatRepositoryPort;
    private final CinemaRepositoryPort cinemaRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<CinemaRoom> getCinemaRooms() {
        return cinemaRoomRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CinemaRoom getCinemaRoomById(UUID id) {
        return cinemaRoomRepository.findById(id).orElseThrow(() -> new CinemaRoomNotFoundException("Cinema Room with id " + id + " not found"));
    }

    @Override
    @Transactional
    public void createCinemaRoom(CinemaRoom cinemaRoom, List<Seat> seats) {
        cinemaRoomRepository.findByName(cinemaRoom.name(), cinemaRoom.cinema().id()).ifPresent(
                a -> {
                    throw new CinemaRoomAlreadyExistsException("Cinema Room with name " + cinemaRoom.name() + " already exists");
                }
        );

        Cinema cinema = null;
        if (cinemaRoom.cinema().id() != null) {
            cinema = cinemaRepositoryPort.findById(cinemaRoom.cinema().id())
                    .orElseThrow(() -> new CinemaNotFoundException("Cinema with id " + cinemaRoom.cinema().id() + " not found"));
        }

        Set<String> labels = new java.util.HashSet<>();
        Set<String> positions = new java.util.HashSet<>();
        for (Seat seat : seats) {
            if (!labels.add(seat.label()) || !positions.add(seat.posX() + ":" + seat.posY()))
                throw new CinemaRoomCapacityException("Duplicate seat label or position");
        }
        CinemaRoom room = cinemaRoom.create(
                cinemaRoom.name(),
                cinemaRoom.capacity(),
                cinema
        );
        CinemaRoom saved = cinemaRoomRepository.save(room);
        seatRepositoryPort.createSeats(seats, saved);
    }

    @Override
    @Transactional
    public void updateCinemaRoom(UUID id, CinemaRoom cinemaRoom) {
        CinemaRoom original = getCinemaRoomById(id);
        if (cinemaRoomRepository.existsByName(cinemaRoom.name(), id, original.cinema().id())) {
            throw new CinemaRoomAlreadyExistsException("Cinema Room with name " + cinemaRoom.name() + " already exists");
        }
        CinemaRoom room = cinemaRoomRepository.findById(id)
                .orElseThrow(() -> new CinemaRoomNotFoundException("Cinema Room with id " + id + " not found"));

        CinemaRoom update = room.update(cinemaRoom.name());
        cinemaRoomRepository.save(update);
    }

    @Override
    @Transactional
    public void deleteCinemaRoomById(UUID id) {
        cinemaRoomRepository.findById(id).orElseThrow(() -> new CinemaRoomNotFoundException("Cinema Room with id " + id + " not found"));
        cinemaRoomRepository.deleteById(id);
    }

}
