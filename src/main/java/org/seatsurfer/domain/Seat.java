package org.seatsurfer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(
        name = "seats",
        uniqueConstraints = {
            @UniqueConstraint(
                name = "seat_id_unique",
                columnNames = "id"
            )
        }
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
            name = "creation_date",
            nullable = false
    )
    private Date creationDate;

    @Column(
            name = "end_availabilty_date"
    )
    private Date endAvailabilityDate;

    @Column(
            name = "seat_type",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String seatType;

    @Column(
            name = "storey_id"
    )
    private Long storeyId;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(
            name = "booking_id",
            referencedColumnName = "id"
    )
    private List<Booking> bookings = new ArrayList<>();
}
