package com.jg.sistema.show_time.infrastructure.persistence.entities;

import com.jg.sistema.cinema_room.infrastructure.persistence.entities.CinemaRoomEntity;
import com.jg.sistema.movie.infrastructure.persistence.entities.MovieEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "show_times")
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private MovieEntity movie;

    @ManyToOne
    private CinemaRoomEntity cinemaRoom;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
