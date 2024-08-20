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

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    private Storey storey;

    @OneToMany(
            mappedBy = "seat",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Booking> bookings = new ArrayList<>();

    // Add Booking to Seat
    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setSeat(this);
    }

    // Remove Booking from Seat
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.setSeat(null);
    }
}
