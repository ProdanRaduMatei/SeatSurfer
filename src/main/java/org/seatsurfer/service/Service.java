package org.seatsurfer.service;

import org.seatsurfer.domain.Seat;
import org.seatsurfer.domain.Storey;
import org.seatsurfer.domain.User;
import org.seatsurfer.persistence.SeatRepository;
import org.seatsurfer.persistence.StoreyRepository;
import org.seatsurfer.persistence.UserRepository;
import org.seatsurfer.persistence.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuildingRepository buildingsRepository;

    @Autowired
    private StoreyRepository storeysRepository;

    @Autowired
    private SeatRepository seatsRepository;

    // Admin services
    // Admin adds a new storey to a building
    public Storey addStorey(Long buildingId, Long userId) {
        if (userRepository.findById(userId).get().getIsAdmin()) {
//            Storey storey = new Storey(buildingId);
//            return storeysRepository.save(storey);
        }
        return null;
    }

    // Admin edits a storey
    public void editStorey(Long storeyId, Long userId, Long buildingId) {
        if (userRepository.findById(userId).get().getIsAdmin()) {
            Storey storey = storeysRepository.findById(storeyId).get();
//            storey.setBuilding(buildingId);
        }
    }

    // Admin deletes a storey
    public void removeStorey(Long storeyId, Long userId) {
        if (userRepository.findById(userId).get().getIsAdmin())
            storeysRepository.deleteById(storeyId);
    }

    // Admin adds a new seat to a storey
    public Seat addSeat(Long storeyId, Long adminId, Integer row, Integer col, Boolean isSeat, Boolean isOccupied, Date occupiedDate) {
        if (userRepository.findById(adminId).get().getIsAdmin()) {
//            Seat seat = new Seat(row, col, isSeat, isOccupied, occupiedDate, storeyId, null, adminId);
//            return seatsRepository.save(seat);
        }
        return null;
    }

    // Admin edits a seat
    public void editSeat(Long seatId, Long adminId, Long storeyId, Integer row, Integer col, Boolean isSeat, Boolean isOccupied, Date occupiedDate, Long userId) {
        if (userRepository.findById(adminId).get().getIsAdmin()) {
            Seat seat = seatsRepository.findById(seatId).get();
            seat.setRow(row);
            seat.setCol(col);
            seat.setIsSeat(isSeat);
            seat.setIsOccupied(isOccupied);
//            seat.setStorey(storeyId);
            if (isOccupied) {
//                seat.setUser(userId);
                seat.setOccupiedDate(occupiedDate);
            }
            else {
                seat.setUser(null);
                seat.setOccupiedDate(null);
            }
            seatsRepository.save(seat);
        }
    }

    // Admin deletes a seat
    public void removeSeat(Long seatId, Long adminId) {
        if (userRepository.findById(adminId).get().getIsAdmin())
            seatsRepository.deleteById(seatId);
    }

    // Admin gets all booked seats in a storey
    public List<Seat> getBookedSeatsInStorey(Long adminId, Long storeyId) {
        if (!userRepository.findById(adminId).get().getIsAdmin())
            return null;
        List<Seat> bookedSeats = new ArrayList<>();
        List<Seat> allSeats = seatsRepository.findAll();
        for (Seat seat : allSeats)
            if (seat.getStorey().equals(storeyId) && seat.getIsOccupied())
                bookedSeats.add(seat);
        return bookedSeats;
    }

    // Admin gets all booked seats in a building
    public List<Seat> getBookedSeatsInBuilding(Long adminId, Long buildingId) {
        if (!userRepository.findById(adminId).get().getIsAdmin())
            return null;
        List<Seat> bookedSeats = new ArrayList<>();
        List<Seat> allSeats = seatsRepository.findAll();
        Map<Long, Storey> storeyCache = new HashMap<>();
        for (Seat seat : allSeats) {
//            Long storeyId = seat.getStorey();
//            Storey storey = storeyCache.computeIfAbsent(storeyId, id -> storeysRepository.findById(id).orElse(null));
//            if (storey != null && storey.getBuilding().equals(buildingId) && seat.getIsOccupied())
                bookedSeats.add(seat);
        }
        return bookedSeats;
    }

    // User books a seat
    public void bookSeat(Long userId, Long seatId, Date date) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Seat seat = seatsRepository.findById(seatId).orElseThrow(() -> new RuntimeException("Seat not found"));

        if (!seat.getIsSeat())
            throw new RuntimeException("Seat not available");
        else if (seat.getIsOccupied())
            throw new RuntimeException("Seat already booked");
        else {
        seat.setIsOccupied(true);
        seat.setOccupiedDate(date);
//        seat.setUser(userId);
}
        seatsRepository.save(seat);
    }

    // User updates a booking
    public void updateBooking(Long userId, Long seatId, Date occupiedDate) {
        Optional<Seat> optionalSeat = seatsRepository.findById(seatId);
        if (optionalSeat.isPresent()) {
            Seat seat = optionalSeat.get();
            if (seat.getUser().equals(userId) && seat.getIsOccupied()) {
                seat.setOccupiedDate(occupiedDate);
                seatsRepository.save(seat);
            }
        }
    }

    // User cancels a booking
    public void cancelBooking(Long userId, Long seatId) {
        Optional<Seat> optionalSeat = seatsRepository.findById(seatId);
        if (optionalSeat.isPresent()) {
            Seat seat = optionalSeat.get();
            if (seat.getUser().equals(userId) && seat.getIsOccupied()) {
                seat.setIsOccupied(false);
                seat.setUser(null);
                seat.setOccupiedDate(null);
                seatsRepository.save(seat);
            }
        }
    }

    // User gets all available seats in a storey
    public List<Seat> getAvailableSeatsInStorey(Long userId, Long storeyId) {
        List<Seat> availableSeats = new ArrayList<>();
        List<Seat> allSeats = seatsRepository.findAll();
        for (Seat seat : allSeats)
            if (seat.getStorey().equals(storeyId) && seat.getIsSeat() && !seat.getIsOccupied())
                availableSeats.add(seat);
        return availableSeats;
    }

    // User gets all available seats in a building
    public List<Seat> getAvailableSeatsInBuilding(Long userId, Long buildingId) {
        List<Seat> availableSeats = new ArrayList<>();
        List<Seat> allSeats = seatsRepository.findAll();
        Map<Long, Storey> storeyCache = new HashMap<>();
        for (Seat seat : allSeats) {
//            Long storeyId = seat.getStorey();
//            Storey storey = storeyCache.computeIfAbsent(storeyId, id -> storeysRepository.findById(id).orElse(null));
//            if (storey != null && storey.getBuilding().equals(buildingId) && seat.getIsSeat() && !seat.getIsOccupied())
                availableSeats.add(seat);
        }
        return availableSeats;
    }
}