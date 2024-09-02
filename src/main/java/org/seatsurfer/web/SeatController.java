package org.seatsurfer.web;

import org.seatsurfer.domain.Seat;
import org.seatsurfer.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/seats")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @GetMapping
    public List<Seat> getAllSeats() {
        return seatService.getAllSeats();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable Long id) {
        Optional<Seat> seat = seatService.getSeatById(id);
        return seat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Seat createSeat(@RequestBody Seat seat) {
        return seatService.createSeat(seat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seat> updateSeat(@PathVariable Long id, @RequestBody Seat seatDetails) {
        Seat seat = seatService.updateSeat(id, seatDetails);
        return ResponseEntity.ok(seat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/empty")
    public List<Seat> getEmptySeats() {
        return seatService.getEmptySeats();
    }

    @GetMapping("/user")
    public String getUserNameBooking(@RequestBody Integer col, @RequestBody Integer line, @RequestBody Instant date) {
        Seat seat = seatService.getSeatByColAndLine(col, line);
        if (seat.getBookings().stream().noneMatch(booking -> booking.getDate().equals(date))) {
            return "No booking found";
        }
        return seat.getBookings().get(0).getUserName();
    }
}
