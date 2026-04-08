package com.seatsurfer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "booked_by_name", nullable = false, length = 160)
    private String bookedByName;

    @Column(name = "booked_by_normalized_name", nullable = false, length = 160)
    private String bookedByNormalizedName;

    @Column(name = "booked_by_email", length = 200)
    private String bookedByEmail;

    public Long getId() {
        return id;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookedByName() {
        return bookedByName;
    }

    public void setBookedByName(String bookedByName) {
        this.bookedByName = bookedByName;
    }

    public String getBookedByNormalizedName() {
        return bookedByNormalizedName;
    }

    public void setBookedByNormalizedName(String bookedByNormalizedName) {
        this.bookedByNormalizedName = bookedByNormalizedName;
    }

    public String getBookedByEmail() {
        return bookedByEmail;
    }

    public void setBookedByEmail(String bookedByEmail) {
        this.bookedByEmail = bookedByEmail;
    }
}
