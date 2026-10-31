package com.jg.sistema.show_time.application.port.out;

import com.jg.sistema.show_time.domain.model.ShowTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShowTimeRepositoryPort {
    List<ShowTime> findAllShowTimes();
    List<ShowTime> findShowTimeMovieIdAndStarTimeAfter(UUID movie_id, LocalDateTime time);
    List<ShowTime> findShowTimesByMovieIdsAndStartTimeAfter(List<UUID> movieIds, LocalDateTime time);
    Optional<ShowTime> findShowTimeById(UUID id);
    void saveShowTime(ShowTime showTime);
    boolean existsOverlappingShowTime(UUID cinemaRoomId, LocalDateTime startTime, LocalDateTime endTime, UUID excludeId);
}
