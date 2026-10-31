package com.jg.sistema.movie.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "movies")
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String description;
    private String posterUrl;
    private String classification;
    private Integer duration;
    private String genre;

    private Boolean isNewRelease;
    private Boolean isPreSale;
    private LocalDateTime openingDate;
}
