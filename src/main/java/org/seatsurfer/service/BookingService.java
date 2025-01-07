package org.seatsurfer.service;

import org.seatsurfer.domain.Booking;
import org.seatsurfer.domain.Seat;
import org.seatsurfer.domain.User;
import org.seatsurfer.persistence.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.seatsurfer.service.SeatService;
import org.seatsurfer.service.UserService;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final SeatService seatService;
    private final UserService userService;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          UserService userService,
                          SeatService seatService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.seatService = seatService;
    }

    public boolean bookSeat(Long userId, Long seatId, Instant date) {
        // 1. Ia user-ul
        User user = userService.getUserById(userId).orElse(null);
        if (user == null) {
            return false; // sau arunci excepție
        }

        // 2. Ia seat-ul
        Seat seat = seatService.getSeatById(seatId).orElse(null);
        if (seat == null) {
            return false; // sau excepție
        }

        // 3. Verifică dacă locul e deja ocupat la data respectivă
        if (!isSeatAvailable(seatId, date)) {
            return false;
        }

        // 4. Creează booking-ul
        Booking booking = new Booking();
        booking.setDate(date);
        booking.setConfirmed(false);  // sau cum vrei tu
        booking.setUser(user);
        booking.setSeat(seat);

        // 5. Salvează în BD
        bookingRepository.save(booking);

        return true;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking bookingDetails) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        booking.setSeat(bookingDetails.getSeat());
        booking.setDate(bookingDetails.getDate());
        booking.setSeat(bookingDetails.getSeat());
        booking.setUser(bookingDetails.getUser());
        booking.setConfirmed(bookingDetails.isConfirmed());
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    //Check if the seat is available for a given date
    public boolean isSeatAvailable(Long seatId, Instant date) {
        return bookingRepository.findBySeatIdAndDate(seatId, date).isEmpty();
    }

    //Temporarily reserve a seat (status remains unconfrimed)
    public boolean reserveSeat(Booking booking) {
        if (isSeatAvailable(booking.getSeat().getId(), booking.getDate())) {
            booking.setConfirmed(false);
            bookingRepository.save(booking);
            return true;
        }
        return false;
    }

    //Confirm the booking
    public boolean confirmBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).map(booking -> {
            booking.setConfirmed(true);
            bookingRepository.save(booking);
            return true;
        }).orElse(false);
    }
}