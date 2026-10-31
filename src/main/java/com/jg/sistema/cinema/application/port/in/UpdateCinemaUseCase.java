package com.jg.sistema.cinema.application.port.in;

import com.jg.sistema.cinema.domain.model.Cinema;

import java.util.Set;
import java.util.UUID;

public interface UpdateCinemaUseCase {
    void updateCinema(UUID id, Cinema cinema, Set<Long> formatIds);
}
