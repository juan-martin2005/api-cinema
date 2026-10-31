package com.jg.sistema.cinema.domain.model;

import java.util.Set;
import java.util.UUID;

public record Cinema(
        UUID id,
        String name,
        String img,
        String address,
        String secondAddress,
        String city,
        Double latitude,
        Double longitude,
        Set<Format> formats,
        Integer roomQuantity,
        String description
) {
    public static Cinema withId(UUID id) {
        return new Cinema(id, null, null, null, null, null, null, null, null, null, null);
    }

    public Cinema withImg(String newImg) {
        return new Cinema(id, name, newImg, address, secondAddress, city, latitude, longitude, formats, roomQuantity, description);
    }

    public Cinema create(String name, String img, String address, String secondAddress, String city,
                         Double latitude, Double longitude, Set<Format> formats, Integer roomQuantity, String description) {
        return new Cinema(id, name, img, address, secondAddress, city, latitude, longitude, formats, roomQuantity, description);
    }

    public Cinema update(String name, String img, String address, String secondAddress, String city,
                         Double latitude, Double longitude, Set<Format> formats, Integer roomQuantity, String description) {
        return new Cinema(id, name, img, address, secondAddress, city, latitude, longitude, formats, roomQuantity, description);
    }
}
