package com.jg.sistema.cinema_room.application.port.out;

import com.jg.sistema.cinema_room.domain.model.CinemaRoom;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CinemaRoomRepositoryPort {
    List<CinemaRoom> findAll();
    Optional<CinemaRoom> findById(UUID id);
    Optional<CinemaRoom> findByName(String name, UUID cinemaId);
    CinemaRoom save(CinemaRoom cinemaRoom);
    void deleteById(UUID id);
    boolean existsByName(String name, UUID id, UUID cinemaId);
}
