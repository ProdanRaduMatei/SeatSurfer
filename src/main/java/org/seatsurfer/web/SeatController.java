package org.seatsurfer.web;

import org.seatsurfer.domain.Seat;
import org.seatsurfer.service.SeatService;
import org.seatsurfer.transfer.BookedSeats;
import org.seatsurfer.transfer.EmptySeats;
import org.seatsurfer.transfer.SeatDTO;
import org.seatsurfer.transfer.UserNameBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/seats")
public class SeatController {
    @Autowired
    private SeatService seatService;

    private EmptySeats emptySeatsRequest;
    private BookedSeats bookedSeatsRequest;

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
    public List<SeatDTO> getEmptySeats(@RequestParam String storeyName, @RequestParam String date) {
        return emptySeatsRequest.getEmptySeats(storeyName, date);
    }

    @GetMapping("/booked")
    public List<SeatDTO> getBookedSeats(@RequestBody String storeyName, @RequestBody String date) {
        return bookedSeatsRequest.getBookedSeats(storeyName, date);
    }

    @GetMapping("/nrOfBookedSeats")
    public Integer getNrOfBookedSeats(@RequestBody String storeyName, @RequestBody String date) {
        return bookedSeatsRequest.getBookedSeats(storeyName, date).size();
    }

    @GetMapping("/nrOfEmptySeats")
    public Integer getNrOfEmptySeats(@RequestParam String storeyName, @RequestParam String date) {
        return emptySeatsRequest.getEmptySeats(storeyName, date).size();
    }

    @GetMapping("/nrOfSeats")
    public Integer getNrOfSeats(@RequestParam String storeyName, @RequestParam String date) {
        return emptySeatsRequest.getEmptySeats(storeyName, date).size() + bookedSeatsRequest.getBookedSeats(storeyName, date).size();
    }
}
