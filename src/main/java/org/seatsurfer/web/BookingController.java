package org.seatsurfer.web;

import org.seatsurfer.domain.Seat;
import org.seatsurfer.service.BookingService;
import org.seatsurfer.service.SeatService;
import org.seatsurfer.transfer.UserNameBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.seatsurfer.domain.Booking;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private SeatService seatService;

    private UserNameBooking userNameBooking;

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    //Endpoint to check seat availability
    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkAvailability(@RequestParam Long seatId, @RequestParam String date) {
        Instant bookingDate = Instant.parse(date);
        boolean isAvailable = bookingService.isSeatAvailable(seatId, bookingDate);
        return ResponseEntity.ok(isAvailable);
    }

    //Endpoint to reserve a seat (temporarily holds the seat for user confirmation)
    @PostMapping("/reserve")
    public ResponseEntity<String> reserveSeat(@RequestBody Booking booking) {
        boolean isReserved = bookingService.reserveSeat(booking);
        return isReserved ? ResponseEntity.ok("Seat reserved successfully.") : ResponseEntity.badRequest().body("Seat is already booked.");
    }

    //Endpoint to confirm the booking
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmBooking(@RequestBody Long bookingId) {
        boolean isConfirmed = bookingService.confirmBooking(bookingId);
        return isConfirmed ? ResponseEntity.ok("Booking confirmed.") : ResponseEntity.badRequest().body("Failed to confirm booking.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking bookingDetails) {
        Booking booking = bookingService.updateBooking(id, bookingDetails);
        return ResponseEntity.ok(booking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/book")
    public Booking bookSeat(@RequestBody Instant date, @RequestBody String userName, @RequestBody String email, @RequestBody Integer col, @RequestBody Integer line) {
        Seat seat = seatService.getSeatByColAndLine(col, line);
        Booking booking = new Booking(date, userName, email, seat);
        return bookingService.createBooking(booking);
    }

    @GetMapping("/user")
    public String getUserNameBooking(@RequestBody Integer col, @RequestBody Integer line, @RequestBody Instant date) {
        return userNameBooking.getUserName(col, line, date);
    }
}