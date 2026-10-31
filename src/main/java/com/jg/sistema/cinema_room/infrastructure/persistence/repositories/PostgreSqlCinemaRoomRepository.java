package com.jg.sistema.cinema_room.infrastructure.persistence.repositories;

import com.jg.sistema.cinema_room.infrastructure.persistence.entities.CinemaRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PostgreSqlCinemaRoomRepository extends JpaRepository<CinemaRoomEntity, UUID> {
    Optional<CinemaRoomEntity> findByNameAndCinema_Id(String name, UUID cinemaId);

    @Query("SELECT CASE " +
            "WHEN COUNT(cr) > 0 THEN true " +
            "ELSE false " +
            "END " +
            "FROM CinemaRoomEntity cr where cr.id != :id and cr.cinema.id = :cinemaId and cr.name = :name")
    boolean existsByName(String name, UUID id, UUID cinemaId);
}
