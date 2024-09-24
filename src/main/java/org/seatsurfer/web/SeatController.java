package org.seatsurfer.web;

import org.seatsurfer.domain.Seat;
import org.seatsurfer.service.SeatService;
import org.seatsurfer.transfer.AllSeats;
import org.seatsurfer.transfer.BookedSeats;
import org.seatsurfer.transfer.EmptySeats;
import org.seatsurfer.transfer.SeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/seats")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @Autowired
    private EmptySeats emptySeatsRequest;

    @Autowired
    private BookedSeats bookedSeatsRequest;

    @Autowired
    private AllSeats allSeatsRequest;

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

    @GetMapping("/all")
    public int[][] getAllSeats(@RequestParam String storeyName, @RequestParam String date) {
        List<SeatDTO> allSeats = allSeatsRequest.getAllSeats(storeyName, date);
        List<SeatDTO> bookedSeats = bookedSeatsRequest.getBookedSeats(storeyName, date);
        int[][] seatMatrix = new int[24][24];

        for (SeatDTO seat : allSeats) {
            int col = seat.getCol();
            int line = seat.getLine();
            if (col >= 0 && col < 24 && line >= 0 && line < 24) {
                seatMatrix[col][line] = 1; // Default to empty
            }
        }

        for (SeatDTO seat : bookedSeats) {
            int col = seat.getCol();
            int line = seat.getLine();
            if (col >= 0 && col < 24 && line >= 0 && line < 24) {
                seatMatrix[col][line] = 2; // Mark as occupied
            }
        }

        return seatMatrix;
    }

    @GetMapping("/empty")
    public int[][] getEmptySeats(@RequestParam String storeyName, @RequestParam String date) {
        List<SeatDTO> emptySeats = emptySeatsRequest.getEmptySeats(storeyName, date);
        int[][] seatMatrix = new int[24][24];

        for (SeatDTO seat : emptySeats) {
            int col = seat.getCol();
            int line = seat.getLine();
            if (col >= 0 && col < 24 && line >= 0 && line < 24) {
                seatMatrix[col][line] = 1;
            }
        }

        return seatMatrix;
    }

    @GetMapping("/booked")
    public int[][] getBookedSeats(@RequestParam String storeyName, @RequestParam String date) {
        List<SeatDTO> bookedSeats = bookedSeatsRequest.getBookedSeats(storeyName, date);
        int[][] seatMatrix = new int[24][24];

        for (SeatDTO seat : bookedSeats) {
            int col = seat.getCol();
            int line = seat.getLine();
            if (col >= 0 && col < 24 && line >= 0 && line < 24) {
                seatMatrix[col][line] = 2; // Mark as occupied
            }
        }

        return seatMatrix;
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