package org.seatsurfer.services;

import org.seatsurfer.domain.Buildings;
import org.seatsurfer.domain.Seats;
import org.seatsurfer.domain.Storeys;
import org.seatsurfer.domain.Users;
import org.seatsurfer.repositories.SeatRepository;
import org.seatsurfer.repositories.StoreyRepository;
import org.seatsurfer.repositories.UsersRepository;
import org.seatsurfer.repositories.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Services {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BuildingRepository buildingsRepository;

    @Autowired
    private StoreyRepository storeysRepository;

    @Autowired
    private SeatRepository seatsRepository;

    // Admin services
    // Admin adds a new storey to a building
    public Storeys addStorey(Long buildingId,  Long userId) {
        if (usersRepository.findById(userId).get().getIsAdmin()) {
            Storeys storey = new Storeys(buildingId);
            return storeysRepository.save(storey);
        }
        return null;
    }

    // Admin edits a storey
    public void editStorey(Long storeyId, Long userId, Long buildingId) {
        if (usersRepository.findById(userId).get().getIsAdmin()) {
            Storeys storey = storeysRepository.findById(storeyId).get();
            storey.setBuildingId(buildingId);
        }
    }

    // Admin deletes a storey
    public void removeStorey(Long storeyId, Long userId) {
        if (usersRepository.findById(userId).get().getIsAdmin())
            storeysRepository.deleteById(storeyId);
    }

    // Admin adds a new seat to a storey
    public Seats addSeat(Long storeyId, Long adminId, Integer row, Integer col, Boolean isSeat, Boolean isOccupied, Date occupiedDate) {
        if (usersRepository.findById(adminId).get().getIsAdmin()) {
            Seats seat = new Seats(row, col, isSeat, isOccupied, occupiedDate, storeyId, null, adminId);
            return seatsRepository.save(seat);
        }
        return null;
    }

    // Admin edits a seat
    public void editSeat(Long seatId, Long adminId, Long storeyId, Integer row, Integer col, Boolean isSeat, Boolean isOccupied, Date occupiedDate, Long userId) {
        if (usersRepository.findById(adminId).get().getIsAdmin()) {
            Seats seat = seatsRepository.findById(seatId).get();
            seat.setRow(row);
            seat.setCol(col);
            seat.setSeat(isSeat);
            seat.setOccupied(isOccupied);
            seat.setStoreyId(storeyId);
            if (isOccupied) {
                seat.setUserId(userId);
                seat.setOccupiedDate(occupiedDate);
            }
            else {
                seat.setUserId(null);
                seat.setOccupiedDate(null);
            }
            seatsRepository.save(seat);
        }
    }

    // Admin deletes a seat
    public void removeSeat(Long seatId, Long adminId) {
        if (usersRepository.findById(adminId).get().getIsAdmin())
            seatsRepository.deleteById(seatId);
    }

    // Admin gets all booked seats in a storey
    public List<Seats> getBookedSeatsInStorey(Long adminId, Long storeyId) {
        if (!usersRepository.findById(adminId).get().getIsAdmin())
            return null;
        List<Seats> bookedSeats = new ArrayList<>();
        List<Seats> allSeats = seatsRepository.findAll();
        for (Seats seat : allSeats)
            if (seat.getStoreyId().equals(storeyId) && seat.getOccupied())
                bookedSeats.add(seat);
        return bookedSeats;
    }

    // Admin gets all booked seats in a building
    public List<Seats> getBookedSeatsInBuilding(Long adminId, Long buildingId) {
        if (!usersRepository.findById(adminId).get().getIsAdmin())
            return null;
        List<Seats> bookedSeats = new ArrayList<>();
        List<Seats> allSeats = seatsRepository.findAll();
        Map<Long, Storeys> storeyCache = new HashMap<>();
        for (Seats seat : allSeats) {
            Long storeyId = seat.getStoreyId();
            Storeys storey = storeyCache.computeIfAbsent(storeyId, id -> storeysRepository.findById(id).orElse(null));
            if (storey != null && storey.getBuildingId().equals(buildingId) && seat.getOccupied())
                bookedSeats.add(seat);
        }
        return bookedSeats;
    }

    // User books a seat
    public void bookSeat(Long userId, Long seatId, Date date) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Seats seat = seatsRepository.findById(seatId).orElseThrow(() -> new RuntimeException("Seat not found"));

        if (!seat.getSeat())
            throw new RuntimeException("Seat not available");
        else if (seat.getOccupied())
            throw new RuntimeException("Seat already booked");
        else {
        seat.setOccupied(true);
        seat.setOccupiedDate(date);
        seat.setUserId(userId);
}
        seatsRepository.save(seat);
    }

    // User updates a booking
    public void updateBooking(Long userId, Long seatId, Date occupiedDate) {
        Optional<Seats> optionalSeat = seatsRepository.findById(seatId);
        if (optionalSeat.isPresent()) {
            Seats seat = optionalSeat.get();
            if (seat.getUserId().equals(userId) && seat.getOccupied()) {
                seat.setOccupiedDate(occupiedDate);
                seatsRepository.save(seat);
            }
        }
    }

    // User cancels a booking
    public void cancelBooking(Long userId, Long seatId) {
        Optional<Seats> optionalSeat = seatsRepository.findById(seatId);
        if (optionalSeat.isPresent()) {
            Seats seat = optionalSeat.get();
            if (seat.getUserId().equals(userId) && seat.getOccupied()) {
                seat.setOccupied(false);
                seat.setUserId(null);
                seat.setOccupiedDate(null);
                seatsRepository.save(seat);
            }
        }
    }

    // User gets all available seats in a storey
    public List<Seats> getAvailableSeatsInStorey(Long userId, Long storeyId) {
        List<Seats> availableSeats = new ArrayList<>();
        List<Seats> allSeats = seatsRepository.findAll();
        for (Seats seat : allSeats)
            if (seat.getStoreyId().equals(storeyId) && seat.getSeat() && !seat.getOccupied())
                availableSeats.add(seat);
        return availableSeats;
    }

    // User gets all available seats in a building
    public List<Seats> getAvailableSeatsInBuilding(Long userId, Long buildingId) {
        List<Seats> availableSeats = new ArrayList<>();
        List<Seats> allSeats = seatsRepository.findAll();
        Map<Long, Storeys> storeyCache = new HashMap<>();
        for (Seats seat : allSeats) {
            Long storeyId = seat.getStoreyId();
            Storeys storey = storeyCache.computeIfAbsent(storeyId, id -> storeysRepository.findById(id).orElse(null));
            if (storey != null && storey.getBuildingId().equals(buildingId) && seat.getSeat() && !seat.getOccupied())
                availableSeats.add(seat);
        }
        return availableSeats;
    }
}