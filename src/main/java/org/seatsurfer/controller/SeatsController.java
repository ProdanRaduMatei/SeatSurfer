package org.seatsurfer.controller;

import org.seatsurfer.domain.Seats;
import org.seatsurfer.services.SeatsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/seats")
public class SeatsController {
    @Autowired
    private SeatsServices seatsServices;

    @GetMapping
    public List<Seats> getAllSeats() {
        return seatsServices.getAllSeats();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seats> getSeatById(@PathVariable Long id) {
        Optional<Seats> seat = seatsServices.getSeatById(id);
        return seat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Seats createSeat(@RequestBody Seats seat) {
        return seatsServices.createSeat(seat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seats> updateSeat(@PathVariable Long id, @RequestBody Seats seatDetails) {
        Seats seat = seatsServices.updateSeat(id, seatDetails);
        return ResponseEntity.ok(seat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatsServices.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }
}
