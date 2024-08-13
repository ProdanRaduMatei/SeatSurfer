package org.seatsurfer.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Seats")
@Table(
        name = "seats"
)
public class Seat {
    @Id
    @SequenceGenerator(
            name = "seat_sequence",
            sequenceName = "seat_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "seat_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "row",
            nullable = false
    )
    private Integer row;

    @Column(
            name = "col",
            nullable = false
    )
    private Integer col;

    @Column(
            name = "is_seat",
            nullable = false
    )
    private Boolean isSeat;

    @Column(
            name = "is_occupied",
            nullable = false
    )
    private Boolean isOccupied;

    @Column(
            name = "occupied_date",
            nullable = false
    )
    private Date occupiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Storey storey;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User admin;
}
