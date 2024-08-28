package org.seatsurfer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "line", nullable = false)
    private Integer line;

    @Column(name = "col", nullable = false)
    private Integer col;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "end_availabilty_date")
    private Instant endAvailabilityDate;

    @Column(name = "seat_type", nullable = false)
    private String seatType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Storey storey;

    @OneToMany(mappedBy = "seat", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setSeat(this);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.setSeat(null);
    }
}
