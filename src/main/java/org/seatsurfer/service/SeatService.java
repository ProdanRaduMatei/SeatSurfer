package org.seatsurfer.service;

import org.seatsurfer.domain.Seat;
import org.seatsurfer.persistence.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatsRepository;

    public List<Seat> getAllSeats() {
        return seatsRepository.findAll();
    }

    public Optional<Seat> getSeatById(Long id) {
        return seatsRepository.findById(id);
    }

    public Seat createSeat(Seat seat) {
        return seatsRepository.save(seat);
    }

    public Seat updateSeat(Long id, Seat seatDetails) {
        Seat seat = seatsRepository.findById(id).orElseThrow();
        seat.setLine(seatDetails.getLine());
        seat.setCol(seatDetails.getCol());
        seat.setCreationDate(seatDetails.getCreationDate());
        seat.setEndAvailabilityDate(seatDetails.getEndAvailabilityDate());
        seat.setSeatType(seatDetails.getSeatType());
        seat.setStorey(seatDetails.getStorey());
        seat.setBookings(seatDetails.getBookings());
        return seatsRepository.save(seat);
    }

    public void deleteSeat(Long id) {
        seatsRepository.deleteById(id);
    }
}
