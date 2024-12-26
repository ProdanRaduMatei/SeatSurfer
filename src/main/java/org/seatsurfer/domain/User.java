package org.seatsurfer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;  // exemplu: "John Doe"

    @Column(name = "email", nullable = false, unique = true)
    private String email; // exemplu: "john.doe@gmail.com"

    // Relație 1 - n: un user poate avea mai multe booking-uri
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
        booking.setUser(this);
    }

    public void removeBooking(Booking booking) {
        this.bookings.remove(booking);
        booking.setUser(null);
    }
}