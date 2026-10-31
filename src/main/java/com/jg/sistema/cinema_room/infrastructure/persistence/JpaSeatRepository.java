package com.jg.sistema.cinema_room.infrastructure.persistence;

import com.jg.sistema.cinema_room.application.port.out.SeatRepositoryPort;
import com.jg.sistema.cinema_room.domain.model.CinemaRoom;
import com.jg.sistema.cinema_room.domain.model.Seat;
import com.jg.sistema.cinema_room.infrastructure.persistence.entities.CinemaRoomEntity;
import com.jg.sistema.cinema_room.infrastructure.persistence.entities.SeatEntity;
import com.jg.sistema.cinema_room.infrastructure.persistence.mapper.SeatMapper;
import com.jg.sistema.cinema_room.infrastructure.persistence.repositories.PostgreSqlCinemaRoomRepository;
import com.jg.sistema.cinema_room.infrastructure.persistence.repositories.PostgreSqlSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaSeatRepository implements SeatRepositoryPort {

    private final PostgreSqlSeatRepository postgreSQLSeatRepository;
    private final PostgreSqlCinemaRoomRepository postgreSQLCinemaRoomRepository;

    @Override
    public List<Seat> findByCinemaRoomId(UUID cinemaRoomId) {
        return postgreSQLSeatRepository.findByRoomIdOrderByRowAscNumberAsc(cinemaRoomId)
                .stream()
                .map(SeatMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Seat> findById(UUID seatId) {
        return postgreSQLSeatRepository.findById(seatId)
                .map(SeatMapper::toDomain);
    }

    @Override
    @Transactional
    public void createSeats(List<Seat> seat, CinemaRoom cinemaRoom) {
        CinemaRoomEntity roomEntity = postgreSQLCinemaRoomRepository.getReferenceById(cinemaRoom.id());
        List<SeatEntity> seatEntity = SeatMapper.toListEntity(seat, roomEntity);
        postgreSQLSeatRepository.saveAll(seatEntity);
    }
}
