package com.jg.sistema.cinema.application.port.in;

import com.jg.sistema.cinema.domain.model.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GetCinemaUseCase {
    List<Cinema> getAllCinemas();
    Page<Cinema> getCinemasWithFilter(Pageable pageable, String city, String name);
    Cinema getCinemaById(UUID id);
}
