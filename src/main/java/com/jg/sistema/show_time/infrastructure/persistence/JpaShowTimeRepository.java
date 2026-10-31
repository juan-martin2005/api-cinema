package com.jg.sistema.show_time.infrastructure.persistence;

import com.jg.sistema.show_time.application.port.out.ShowTimeRepositoryPort;
import com.jg.sistema.show_time.domain.model.ShowTime;
import com.jg.sistema.show_time.infrastructure.persistence.entities.ShowTimeEntity;
import com.jg.sistema.show_time.infrastructure.persistence.mapper.ShowTimeMapper;
import com.jg.sistema.show_time.infrastructure.persistence.repositories.PostgreSqlShowTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaShowTimeRepository implements ShowTimeRepositoryPort {

    private final PostgreSqlShowTimeRepository repository;

    @Override
    public List<ShowTime> findAllShowTimes() {
        return repository.findAll().stream().map(ShowTimeMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<ShowTime> findShowTimeMovieIdAndStarTimeAfter(UUID movie_id, LocalDateTime time) {
        return repository.findByMovie_IdAndStartTimeAfter(movie_id, time).stream().map(ShowTimeMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<ShowTime> findShowTimesByMovieIdsAndStartTimeAfter(List<UUID> movieIds, LocalDateTime time) {
        if (movieIds == null || movieIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return repository.findByMovie_IdInAndStartTimeAfter(movieIds, time).stream().map(ShowTimeMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<ShowTime> findShowTimeById(UUID id) {
        return repository.findById(id).map(ShowTimeMapper::toDomain);
    }

    @Override
    public void saveShowTime(ShowTime showTime) {
        ShowTimeEntity saveShowTime = ShowTimeMapper.toEntity(showTime);
        repository.save(saveShowTime);
    }

    @Override
    public boolean existsOverlappingShowTime(UUID cinemaRoomId, LocalDateTime startTime, LocalDateTime endTime, UUID excludeId) {
        return repository.existsOverlappingShowTime(cinemaRoomId, startTime, endTime, excludeId);
    }

}
