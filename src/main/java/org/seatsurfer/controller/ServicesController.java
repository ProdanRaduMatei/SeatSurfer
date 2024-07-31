package org.seatsurfer.controller;

import org.seatsurfer.services.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping("api/services")
public class ServicesController {
    @Autowired
    private Services services;

    @PostMapping("/addStorey")
    public void addStorey(@RequestParam Long buildingId, @RequestParam Long userId) {
        services.addStorey(buildingId, userId);
    }

    @PutMapping("/editStorey")
    public void editStorey(@RequestParam Long storeyId, @RequestParam Long userId, @RequestParam Long buildingId) {
        services.editStorey(storeyId, userId, buildingId);
    }

    @DeleteMapping("/removeStorey")
    public void removeStorey(@RequestParam Long storeyId, @RequestParam Long userId) {
        services.removeStorey(storeyId, userId);
    }

    @PostMapping("/addSeat")
    public void addSeat(@RequestParam Long storeyId, @RequestParam Long adminId, @RequestParam Integer row, @RequestParam Integer col, @RequestParam Boolean isSeat, @RequestParam Boolean isOccupied, @RequestParam Date occupiedDate) {
        services.addSeat(storeyId, adminId, row, col, isSeat, isOccupied, occupiedDate);
    }

    @PutMapping("/editSeat")
    public void editSeat(@RequestParam Long seatId, @RequestParam Long adminId, @RequestParam Long storeyId, @RequestParam Integer row, @RequestParam Integer col, @RequestParam Boolean isSeat, @RequestParam Boolean isOccupied, @RequestParam Date occupiedDate, @RequestParam Long userId) {
        services.editSeat(seatId, adminId, storeyId, row, col, isSeat, isOccupied, occupiedDate, userId);
    }

    @DeleteMapping("/removeSeat")
    public void removeSeat(@RequestParam Long seatId, @RequestParam Long userId) {
        services.removeSeat(seatId, userId);
    }

    @GetMapping("/getBookedSeatsInStorey")
    public void getBookedSeatsInStorey(@RequestParam Long userId, @RequestParam Long storeyId) {
        services.getBookedSeatsInStorey(userId, storeyId);
    }

    @GetMapping("/getBookedSeatsInBuilding")
    public void getBookedSeatsInBuilding(@RequestParam Long userId, @RequestParam Long buildingId) {
        services.getBookedSeatsInBuilding(userId, buildingId);
    }

    @PostMapping("/bookSeat")
    public void bookSeat(@RequestParam Long userId, @RequestParam Long seatId, @RequestParam Date bookingDate) {
        services.bookSeat(userId, seatId, bookingDate);
    }

    @PutMapping("/editBooking")
    public void editBooking(@RequestParam Long userId, @RequestParam Long seatId, @RequestParam Date bookingDate) {
        services.updateBooking(userId, seatId, bookingDate);
    }

    @DeleteMapping("/cancelBooking")
    public void cancelBooking(@RequestParam Long userId, @RequestParam Long seatId) {
        services.cancelBooking(userId, seatId);
    }

    @GetMapping("/getAllFreeSeatsInStorey")
    public void getAllFreeSeatsInStorey(@RequestParam Long userId, @RequestParam Long storeyId) {
        services.getAvailableSeatsInStorey(userId, storeyId);
    }

    @GetMapping("/getAllFreeSeatsInBuilding")
    public void getAllFreeSeatsInBuilding(@RequestParam Long userId, @RequestParam Long buildingId) {
        services.getAvailableSeatsInBuilding(userId, buildingId);
    }
}