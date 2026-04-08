package com.seatsurfer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class BookingDtos {

    private BookingDtos() {
    }

    public record CreateBookingRequest(
        @NotNull(message = "Seat id is required")
        Long seatId,
        @NotNull(message = "Booking date is required")
        LocalDate bookingDate,
        @NotBlank(message = "Name is required")
        String bookedByName,
        @Email(message = "Email must be valid")
        String bookedByEmail
    ) {
    }

    public record CancelBookingRequest(
        @NotBlank(message = "Requester name is required")
        String requesterName
    ) {
    }

    public record BookingResponse(
        Long id,
        Long seatId,
        String seatLabel,
        Long floorId,
        String floorName,
        LocalDate bookingDate,
        String bookedByName,
        String bookedByEmail,
        LocalDateTime createdAt
    ) {
    }
}
