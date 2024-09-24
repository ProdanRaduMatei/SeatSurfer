package org.seatsurfer.transfer;

import org.seatsurfer.domain.Booking;
import org.seatsurfer.domain.Seat;
import org.seatsurfer.service.SeatService;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;

public class UserNameBooking {
    private SeatService seatService;

    public String getUserName(@RequestBody Integer col, @RequestBody Integer line, @RequestBody Instant date) {
        Seat seat = seatService.getSeatByColAndLine(col, line);
        return seat.getBookings().stream()
                .filter(booking -> booking.getDate().equals(date))
                .map(Booking::getUserName)
                .findFirst()
                .orElse(null);
    }
}
