package com.jg.sistema.show_time.infrastructure.persistence.repositories;

import com.jg.sistema.show_time.infrastructure.persistence.entities.ShowTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PostgreSqlShowTimeRepository extends JpaRepository<ShowTimeEntity, UUID> {

    List<ShowTimeEntity> findByMovie_IdAndStartTimeAfter(UUID movieId, LocalDateTime time);

    List<ShowTimeEntity> findByMovie_IdInAndStartTimeAfter(List<UUID> movieIds, LocalDateTime time);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM ShowTimeEntity s " +
            "WHERE s.cinemaRoom.id = :cinemaRoomId " +
            "AND (:excludeId IS NULL OR s.id <> :excludeId) " +
            "AND s.startTime < :endTime " +
            "AND s.endTime > :startTime")
    boolean existsOverlappingShowTime(
            UUID cinemaRoomId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            UUID excludeId
    );
}
