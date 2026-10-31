package com.jg.sistema.cinema_room.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "seats")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String row;
    private Integer number;
    @Column(name = "pos_x")
    private Integer posX;
    @Column(name = "pos_y")
    private Integer posY;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private CinemaRoomEntity room;
}
