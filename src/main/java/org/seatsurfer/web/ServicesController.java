package org.seatsurfer.web;

import org.seatsurfer.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping("api/services")
public class ServicesController {
    @Autowired
    private Service service;

    @PostMapping("/addStorey")
    public void addStorey(@RequestParam Long buildingId, @RequestParam Long userId) {
        service.addStorey(buildingId, userId);
    }

    @PutMapping("/editStorey")
    public void editStorey(@RequestParam Long storeyId, @RequestParam Long userId, @RequestParam Long buildingId) {
        service.editStorey(storeyId, userId, buildingId);
    }

    @DeleteMapping("/removeStorey")
    public void removeStorey(@RequestParam Long storeyId, @RequestParam Long userId) {
        service.removeStorey(storeyId, userId);
    }

    @PostMapping("/addSeat")
    public void addSeat(@RequestParam Long storeyId, @RequestParam Long adminId, @RequestParam Integer row, @RequestParam Integer col, @RequestParam Boolean isSeat, @RequestParam Boolean isOccupied, @RequestParam Date occupiedDate) {
        service.addSeat(storeyId, adminId, row, col, isSeat, isOccupied, occupiedDate);
    }

    @PutMapping("/editSeat")
    public void editSeat(@RequestParam Long seatId, @RequestParam Long adminId, @RequestParam Long storeyId, @RequestParam Integer row, @RequestParam Integer col, @RequestParam Boolean isSeat, @RequestParam Boolean isOccupied, @RequestParam Date occupiedDate, @RequestParam Long userId) {
        service.editSeat(seatId, adminId, storeyId, row, col, isSeat, isOccupied, occupiedDate, userId);
    }

    @DeleteMapping("/removeSeat")
    public void removeSeat(@RequestParam Long seatId, @RequestParam Long userId) {
        service.removeSeat(seatId, userId);
    }

    @GetMapping("/getBookedSeatsInStorey")
    public void getBookedSeatsInStorey(@RequestParam Long userId, @RequestParam Long storeyId) {
        service.getBookedSeatsInStorey(userId, storeyId);
    }

    @GetMapping("/getBookedSeatsInBuilding")
    public void getBookedSeatsInBuilding(@RequestParam Long userId, @RequestParam Long buildingId) {
        service.getBookedSeatsInBuilding(userId, buildingId);
    }

    @PostMapping("/bookSeat")
    public void bookSeat(@RequestParam Long userId, @RequestParam Long seatId, @RequestParam Date bookingDate) {
        service.bookSeat(userId, seatId, bookingDate);
    }

    @PutMapping("/editBooking")
    public void editBooking(@RequestParam Long userId, @RequestParam Long seatId, @RequestParam Date bookingDate) {
        service.updateBooking(userId, seatId, bookingDate);
    }

    @DeleteMapping("/cancelBooking")
    public void cancelBooking(@RequestParam Long userId, @RequestParam Long seatId) {
        service.cancelBooking(userId, seatId);
    }

    @GetMapping("/getAllFreeSeatsInStorey")
    public void getAllFreeSeatsInStorey(@RequestParam Long userId, @RequestParam Long storeyId) {
        service.getAvailableSeatsInStorey(userId, storeyId);
    }

    @GetMapping("/getAllFreeSeatsInBuilding")
    public void getAllFreeSeatsInBuilding(@RequestParam Long userId, @RequestParam Long buildingId) {
        service.getAvailableSeatsInBuilding(userId, buildingId);
    }
}