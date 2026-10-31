package com.jg.sistema.booking.infrastructure.persistence.entities;

import com.jg.sistema.booking.domain.model.BookingStatus;
import com.jg.sistema.show_time.infrastructure.persistence.entities.ShowTimeEntity;
import com.jg.sistema.user.infrastructure.persistence.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BookingStatus bookingStatus;

    private String paymentStatus;

    @Column(nullable = false)
    private BigDecimal subTotal;

    @Column(nullable = false)
    private BigDecimal tax;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private LocalDateTime creatAt;

    private LocalDateTime expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShowTimeEntity showTimeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<BookingDetailEntity> details = new ArrayList<>();

    public void addDetail(BookingDetailEntity detail) {
        if (details == null) {
            details = new ArrayList<>();
        }
        details.add(detail);
        detail.setBooking(this);
    }
}
