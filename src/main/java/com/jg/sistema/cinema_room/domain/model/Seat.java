package com.jg.sistema.cinema_room.domain.model;

import java.util.UUID;

public record Seat(
        UUID id,
        Integer number,
        String row,
        Integer posX,
        Integer posY
) {
    public String label() {
        return row + number;
    }

    public Seat create(int number, String row, Integer posX, Integer posY) {
        return new Seat(id, number, row, posX, posY);
    }
}
