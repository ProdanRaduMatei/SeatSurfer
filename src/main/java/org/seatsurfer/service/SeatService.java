package org.seatsurfer.service;

import org.seatsurfer.domain.Seat;
import org.seatsurfer.persistence.SeatRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public Optional<Seat> getSeatById(Long id) {
        return seatRepository.findById(id);
    }

    public Seat createSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public Seat updateSeat(Long id, Seat seatDetails) {
        Seat seat = seatRepository.findById(id).orElseThrow();
        seat.setLine(seatDetails.getLine());
        seat.setCol(seatDetails.getCol());
        seat.setCreationDate(seatDetails.getCreationDate());
        seat.setEndAvailabilityDate(seatDetails.getEndAvailabilityDate());
        seat.setSeatType(seatDetails.getSeatType());
        seat.setStorey(seatDetails.getStorey());
        seat.setBookings(seatDetails.getBookings());
        return seatRepository.save(seat);
    }

    public void deleteSeat(Long id) {
        seatRepository.deleteById(id);
    }

    public List<Seat> getAllSeats(String storeyName, Instant date) {
        return seatRepository.findSeatsByStoreyAndDate(storeyName, date);
    }

    public List<Seat> getEmptySeats(String storeyName, Instant date) {
        return seatRepository.findAll().stream()
                .filter(seat -> seat.getStorey().getName().equals(storeyName))
                .filter(seat -> seat.getBookings().stream().noneMatch(booking -> booking.getDate().equals(date)))
                .toList();
    }

    public List<Seat> getBookedSeats(String storeyName, Instant date) {
        return seatRepository.findAll().stream()
                .filter(seat -> seat.getStorey().getName().equals(storeyName))
                .filter(seat -> seat.getBookings().stream().anyMatch(booking -> booking.getDate().equals(date)))
                .toList();
    }

    public Seat getSeatByColAndLine(Integer col, Integer line) {
        return seatRepository.findAll().stream()
                .filter(seat -> seat.getCol().equals(col) && seat.getLine().equals(line))
                .findFirst()
                .orElseThrow();
    }
}
