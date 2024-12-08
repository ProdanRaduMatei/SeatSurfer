package org.seatsurfer.service;

import org.seatsurfer.domain.Booking;
import org.seatsurfer.persistence.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
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
        booking.setUserName(bookingDetails.getUserName());
        booking.setEmail(bookingDetails.getEmail());
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