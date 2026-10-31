package com.jg.sistema.cinema.application.port.out;

import com.jg.sistema.cinema.domain.model.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CinemaRepositoryPort {
    List<Cinema> findAll();
    Page<Cinema> findAll(Pageable pageable, String city, String name);
    Optional<Cinema> findById(UUID id);
    Optional<Cinema> findByName(String name);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, UUID id);
    Cinema save(Cinema cinema);
    void deleteById(UUID id);
}
