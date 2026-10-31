package com.jg.sistema.cinema_room.infrastructure.persistence;

import com.jg.sistema.cinema_room.application.port.out.CinemaRoomRepositoryPort;
import com.jg.sistema.cinema_room.domain.model.CinemaRoom;
import com.jg.sistema.cinema_room.infrastructure.persistence.entities.CinemaRoomEntity;
import com.jg.sistema.cinema_room.infrastructure.persistence.mapper.CinemaRoomMapper;
import com.jg.sistema.cinema_room.infrastructure.persistence.repositories.PostgreSqlCinemaRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaCinemaRoomRepository implements CinemaRoomRepositoryPort {

    private final PostgreSqlCinemaRoomRepository cinemaRoomRepository;

    @Override
    public List<CinemaRoom> findAll() {
        return cinemaRoomRepository.findAll().stream()
                .map(CinemaRoomMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CinemaRoom> findById(UUID id) {
        return cinemaRoomRepository.findById(id).map(CinemaRoomMapper::toDomain);
    }

    @Override
    public Optional<CinemaRoom> findByName(String name, UUID cinemaId) {
        return cinemaRoomRepository.findByNameAndCinema_Id(name, cinemaId).map(CinemaRoomMapper::toDomain);
    }

    @Override
    public CinemaRoom save(CinemaRoom cinemaRoom) {
        CinemaRoomEntity cinemaRoomEntity = CinemaRoomMapper.toEntity(cinemaRoom);
        CinemaRoomEntity room = cinemaRoomRepository.save(cinemaRoomEntity);
        return CinemaRoomMapper.toDomain(room);
    }

    @Override
    public void deleteById(UUID id) {
        cinemaRoomRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name, UUID id, UUID cinemaId) {
        return cinemaRoomRepository.existsByName(name, id, cinemaId);
    }

}
