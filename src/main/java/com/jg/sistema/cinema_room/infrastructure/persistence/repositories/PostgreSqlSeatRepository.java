package com.jg.sistema.cinema_room.infrastructure.persistence.repositories;

import com.jg.sistema.cinema_room.infrastructure.persistence.entities.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostgreSqlSeatRepository extends JpaRepository<SeatEntity, UUID> {
    List<SeatEntity> findByRoomId(UUID cinemaRoomId);

    List<SeatEntity> findByRoomIdOrderByRowAscNumberAsc(UUID cinemaRoomId);
}
