package org.seatsurfer.transfer;

import org.seatsurfer.domain.Booking;
import org.seatsurfer.domain.Seat;
import org.seatsurfer.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class UserNameBooking {

    @Autowired
    private SeatService seatService;

    // Metoda care extrage "numele" user-ului care a rezervat seat-ul
    public String getUserName(Integer col, Integer line, Instant date) {
        // 1. Obținem seat-ul după col & line
        Seat seat = seatService.getSeatByColAndLine(col, line);

        // 2. Găsim booking-ul pentru data respectivă
        Optional<Booking> matchingBooking = seat.getBookings().stream()
                .filter(booking -> booking.getDate().equals(date))
                .findFirst();

        // 3. Returnăm numele user-ului, dacă există
        //    (acum user-ul e un obiect de tip `User`, deci booking.getUser().getName())
        return matchingBooking
                .map(booking -> {
                    if (booking.getUser() != null) {
                        return booking.getUser().getName();
                    } else {
                        return null;
                    }
                })
                .orElse(null);
    }
}