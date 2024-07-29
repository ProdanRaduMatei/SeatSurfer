package org.seatsurfer.services;

import org.seatsurfer.domain.Seats;
import org.seatsurfer.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatsServices {
    @Autowired
    private SeatRepository seatsRepository;

    public List<Seats> getAllSeats() {
        return seatsRepository.findAll();
    }

    public Optional<Seats> getSeatById(Long id) {
        return seatsRepository.findById(id);
    }

    public Seats createSeat(Seats seat) {
        return seatsRepository.save(seat);
    }

    public Seats updateSeat(Long id, Seats seatDetails) {
        Seats seat = seatsRepository.findById(id).orElseThrow();
        seat.setRow(seatDetails.getRow());
        seat.setCol(seatDetails.getCol());
        seat.setSeat(seatDetails.getSeat());
        seat.setOccupied(seatDetails.getOccupied());
        seat.setOccupiedDate(seatDetails.getOccupiedDate());
        seat.setStoreyId(seatDetails.getStoreyId());
        seat.setUserId(seatDetails.getUserId());
        seat.setAdminId(seatDetails.getAdminId());
        return seatsRepository.save(seat);
    }

    public void deleteSeat(Long id) {
        seatsRepository.deleteById(id);
    }
}
