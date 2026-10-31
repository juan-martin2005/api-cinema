package com.jg.sistema.cinema.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cinemas")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CinemaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String img;

    @Column(nullable = false)
    private String address;

    private String secondAddress;

    @Column(nullable = false)
    private String city;

    private Double latitude;

    private Double longitude;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cinema_formats",
            joinColumns = @JoinColumn(name = "cinema_id"),
            inverseJoinColumns = @JoinColumn(name = "format_id")
    )
    private Set<FormatEntity> formats;

    private Integer roomQuantity;

    @Column(length = 1000)
    private String description;
}
