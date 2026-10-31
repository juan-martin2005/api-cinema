package com.jg.sistema.cinema_room.infrastructure.persistence.entities;

import com.jg.sistema.cinema.infrastructure.persistence.entities.CinemaEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cinema_rooms")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CinemaRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private Integer capacity;
    private Integer seatPerRow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id", referencedColumnName = "id")
    private CinemaEntity cinema;

}
