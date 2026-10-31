package com.jg.sistema.cinema.infrastructure.persistence;

import com.jg.sistema.cinema.application.port.out.CinemaRepositoryPort;
import com.jg.sistema.cinema.domain.model.Cinema;
import com.jg.sistema.cinema.infrastructure.persistence.entities.CinemaEntity;
import com.jg.sistema.cinema.infrastructure.persistence.mapper.CinemaMapper;
import com.jg.sistema.cinema.infrastructure.persistence.repositories.PostgreSqlCinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaCinemaRepository implements CinemaRepositoryPort {

    private final PostgreSqlCinemaRepository postgreSqlCinemaRepository;

    @Override
    public List<Cinema> findAll() {
        return postgreSqlCinemaRepository.findAll().stream()
                .map(CinemaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Cinema> findAll(Pageable pageable, String city, String name) {
        return postgreSqlCinemaRepository.findWithFilter(pageable, city, name)
                .map(CinemaMapper::toDomain);
    }

    @Override
    public Optional<Cinema> findById(UUID id) {
        return postgreSqlCinemaRepository.findById(id)
                .map(CinemaMapper::toDomain);
    }

    @Override
    public Optional<Cinema> findByName(String name) {
        return postgreSqlCinemaRepository.findByNameIgnoreCase(name)
                .map(CinemaMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return postgreSqlCinemaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, UUID id) {
        return postgreSqlCinemaRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    @Override
    public Cinema save(Cinema cinema) {
        CinemaEntity entity = CinemaMapper.toEntity(cinema);
        CinemaEntity saved = postgreSqlCinemaRepository.save(entity);
        return CinemaMapper.toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        postgreSqlCinemaRepository.deleteById(id);
    }
}
