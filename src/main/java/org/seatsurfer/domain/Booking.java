package org.seatsurfer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(
        name = "bookings",
        uniqueConstraints = {
            @UniqueConstraint(
                name = "booking_id_unique",
                columnNames = "id"
            ),
            @UniqueConstraint(
                    name = "booking_username_unique",
                    columnNames = "user_name"
            ),
            @UniqueConstraint(
                    name = "booking_email_unique",
                    columnNames = "email"
            )
        }
)
public class Booking {
    @Id
    @Column(
        name = "id",
        updatable = false
    )
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "user_name", nullable = false, columnDefinition = "TEXT")
    private String userName;

    @Column(name = "email", nullable = false, columnDefinition = "TEXT")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    private Seat seat;
}