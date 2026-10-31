package com.jg.sistema.show_time.application.service;

import com.jg.sistema.cinema_room.application.port.out.CinemaRoomRepositoryPort;
import com.jg.sistema.cinema_room.domain.exception.CinemaRoomNotFoundException;
import com.jg.sistema.cinema_room.domain.model.CinemaRoom;
import com.jg.sistema.movie.application.port.out.MovieRepositoryPort;
import com.jg.sistema.movie.domain.exception.MovieNotFoundException;
import com.jg.sistema.movie.domain.model.Movie;
import com.jg.sistema.show_time.application.port.in.CreateShowTimeUseCase;
import com.jg.sistema.show_time.application.port.in.GetShowTimeUseCase;
import com.jg.sistema.show_time.application.port.in.UpdateShowTimeUseCase;
import com.jg.sistema.show_time.application.port.out.ShowTimeRepositoryPort;
import com.jg.sistema.show_time.domain.exception.ShowTimeAlreadyExistException;
import com.jg.sistema.show_time.domain.exception.ShowTimeNotFoundException;
import com.jg.sistema.show_time.domain.model.ShowTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowTimeService implements GetShowTimeUseCase, CreateShowTimeUseCase, UpdateShowTimeUseCase {

    private final ShowTimeRepositoryPort showTimeRepositoryPort;
    private final MovieRepositoryPort movieRepositoryPort;
    private final CinemaRoomRepositoryPort cinemaRoomRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<ShowTime> getAllShowTimes() {
        return showTimeRepositoryPort.findAllShowTimes();
    }

    @Override
    public List<ShowTime> getShowTimeByMovieIdAndStartTimeAfter(UUID movieId, LocalDateTime time) {
        return showTimeRepositoryPort.findShowTimeMovieIdAndStarTimeAfter(movieId, time);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowTime> getShowTimesByMovieIdsAndStartTimeAfter(List<UUID> movieIds, LocalDateTime time) {
        if (movieIds == null || movieIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return showTimeRepositoryPort.findShowTimesByMovieIdsAndStartTimeAfter(movieIds, time);
    }

    @Override
    @Transactional(readOnly = true)
    public ShowTime getShowTimedById(UUID id) {
        return showTimeRepositoryPort.findShowTimeById(id)
                .orElseThrow(() -> new ShowTimeNotFoundException("ShowTime not found with id: " + id));
    }


    @Override
    @Transactional
    public void createShowTime(ShowTime showTime) {
        Movie movie = movieRepositoryPort.findById(showTime.movie().id())
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + showTime.movie().id()));
        CinemaRoom cinemaRoom = cinemaRoomRepositoryPort.findById(showTime.cinemaRoom().id())
                .orElseThrow(() -> new CinemaRoomNotFoundException("Cinema room not found with id: " + showTime.cinemaRoom().id()));

        LocalDateTime startTime = showTime.startTime();
        LocalDateTime endTime = startTime.plusMinutes(movie.duration());

        validateShowTimeOverlap(cinemaRoom.id(), startTime, endTime, null);

        ShowTime createdShowTime = showTime.create(
                movie,
                cinemaRoom,
                startTime
        );
        showTimeRepositoryPort.saveShowTime(createdShowTime);
    }

    @Override
    @Transactional
    public void updateShowTime(UUID id, ShowTime showTime) {
        ShowTime existing = showTimeRepositoryPort.findShowTimeById(id)
                .orElseThrow(() -> new ShowTimeNotFoundException("ShowTime not found with id: " + id));

        Movie movie = (showTime.movie() != null && showTime.movie().id() != null)
                ? movieRepositoryPort.findById(showTime.movie().id())
                    .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + showTime.movie().id()))
                : existing.movie();

        CinemaRoom cinemaRoom = (showTime.cinemaRoom() != null && showTime.cinemaRoom().id() != null)
                ? cinemaRoomRepositoryPort.findById(showTime.cinemaRoom().id())
                    .orElseThrow(() -> new CinemaRoomNotFoundException("Cinema room not found with id: " + showTime.cinemaRoom().id()))
                : existing.cinemaRoom();

        LocalDateTime startTime = showTime.startTime() != null ? showTime.startTime() : existing.startTime();
        LocalDateTime endTime = startTime.plusMinutes(movie.duration());

        validateShowTimeOverlap(cinemaRoom.id(), startTime, endTime, id);

        ShowTime updatedShowTime = existing.update(movie, cinemaRoom, startTime);
        showTimeRepositoryPort.saveShowTime(updatedShowTime);
    }

    private void validateShowTimeOverlap(UUID cinemaRoomId, LocalDateTime startTime, LocalDateTime endTime, UUID excludeId) {
        boolean existsOverlap = showTimeRepositoryPort.existsOverlappingShowTime(cinemaRoomId, startTime, endTime, excludeId);
        if (existsOverlap) {
            throw new ShowTimeAlreadyExistException("There is already a showtime scheduled in this cinema room that overlaps with the selected time interval.");
        }
    }
}
