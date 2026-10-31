package com.jg.sistema.cinema.application.port.in;

import com.jg.sistema.cinema.domain.model.Cinema;

import java.util.Set;

public interface CreateCinemaUseCase {
    void createCinema(Cinema cinema, Set<Long> formatIds);
}
