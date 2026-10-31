package com.jg.sistema.show_time.application.port.in;

import com.jg.sistema.show_time.domain.model.ShowTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GetShowTimeUseCase {
    ShowTime getShowTimedById(UUID id);
    List<ShowTime> getAllShowTimes();
    List<ShowTime> getShowTimeByMovieIdAndStartTimeAfter(UUID movieId, LocalDateTime time);
    List<ShowTime> getShowTimesByMovieIdsAndStartTimeAfter(List<UUID> movieIds, LocalDateTime time);
}
