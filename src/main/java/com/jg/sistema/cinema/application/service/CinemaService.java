package com.jg.sistema.cinema.application.service;

import com.jg.sistema.cinema.application.port.in.CreateCinemaUseCase;
import com.jg.sistema.cinema.application.port.in.DeleteCinemaUseCase;
import com.jg.sistema.cinema.application.port.in.GetCinemaUseCase;
import com.jg.sistema.cinema.application.port.in.UpdateCinemaUseCase;
import com.jg.sistema.cinema.application.port.out.CinemaRepositoryPort;
import com.jg.sistema.cinema.application.port.out.FormatRepositoryPort;
import com.jg.sistema.cinema.domain.exception.CinemaAlreadyExistsException;
import com.jg.sistema.cinema.domain.exception.CinemaNotFoundException;
import com.jg.sistema.cinema.domain.model.Cinema;
import com.jg.sistema.cinema.domain.model.Format;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CinemaService implements GetCinemaUseCase, CreateCinemaUseCase, UpdateCinemaUseCase, DeleteCinemaUseCase {

    private final CinemaRepositoryPort cinemaRepositoryPort;
    private final FormatRepositoryPort formatRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<Cinema> getAllCinemas() {
        return cinemaRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cinema> getCinemasWithFilter(Pageable pageable, String city, String name) {
        return cinemaRepositoryPort.findAll(pageable, city, name);
    }

    @Override
    @Transactional(readOnly = true)
    public Cinema getCinemaById(UUID id) {
        return cinemaRepositoryPort.findById(id)
                .orElseThrow(() -> new CinemaNotFoundException("Cinema not found with id: " + id));
    }

    @Override
    @Transactional
    public void createCinema(Cinema cinema, Set<Long> formatIds) {
        if (cinemaRepositoryPort.existsByName(cinema.name())) {
            throw new CinemaAlreadyExistsException("Cinema with name '" + cinema.name() + "' already exists");
        }

        Set<Format> formats = resolveFormats(formatIds);
        Cinema newCinema = cinema.create(
                cinema.name(),
                cinema.img(),
                cinema.address(),
                cinema.secondAddress(),
                cinema.city(),
                cinema.latitude(),
                cinema.longitude(),
                formats,
                cinema.roomQuantity(),
                cinema.description()
        );
        cinemaRepositoryPort.save(newCinema);
    }

    @Override
    @Transactional
    public void updateCinema(UUID id, Cinema cinema, Set<Long> formatIds) {
        Cinema existing = cinemaRepositoryPort.findById(id)
                .orElseThrow(() -> new CinemaNotFoundException("Cinema not found with id: " + id));

        if (cinemaRepositoryPort.existsByNameAndIdNot(cinema.name(), id)) {
            throw new CinemaAlreadyExistsException("Cinema with name '" + cinema.name() + "' already exists");
        }

        Set<Format> formats = formatIds != null ? resolveFormats(formatIds) : existing.formats();
        Cinema updatedCinema = existing.update(
                cinema.name(),
                cinema.img() != null ? cinema.img() : existing.img(),
                cinema.address(),
                cinema.secondAddress(),
                cinema.city(),
                cinema.latitude(),
                cinema.longitude(),
                formats,
                cinema.roomQuantity(),
                cinema.description()
        );
        cinemaRepositoryPort.save(updatedCinema);
    }

    @Override
    @Transactional
    public void deleteCinema(UUID id) {
        cinemaRepositoryPort.findById(id)
                .orElseThrow(() -> new CinemaNotFoundException("Cinema not found with id: " + id));
        cinemaRepositoryPort.deleteById(id);
    }

    private Set<Format> resolveFormats(Set<Long> formatIds) {
        if (formatIds == null || formatIds.isEmpty()) {
            return Collections.emptySet();
        }
        return formatRepositoryPort.findByIds(formatIds);
    }
}
