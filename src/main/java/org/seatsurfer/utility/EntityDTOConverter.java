package org.seatsurfer.utility;

import org.seatsurfer.domain.Booking;
import org.seatsurfer.domain.Seat;
import org.seatsurfer.domain.Storey;
import org.seatsurfer.transfer.BookingDTO;
import org.seatsurfer.transfer.SeatDTO;
import org.seatsurfer.transfer.StoreyDTO;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.List;

public class EntityDTOConverter {

    public static BookingDTO convertToBookingDTO(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .userName(booking.getUserName())
                .email(booking.getEmail())
                .seat(convertToSeatDTO(booking.getSeat()))
                .date(booking.getDate().toString())
                .build();
    }

    public static Booking convertToBookingEntity(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setId(bookingDTO.getId());
        booking.setUserName(bookingDTO.getUserName());
        booking.setEmail(bookingDTO.getEmail());
        booking.setSeat(convertToSeatEntity(bookingDTO.getSeat()));
        booking.setDate(Instant.parse(bookingDTO.getDate()));
        return booking;
    }

    public static SeatDTO convertToSeatDTO(Seat seat) {
        return SeatDTO.builder()
                .id(seat.getId())
                .col(seat.getCol())
                .line(seat.getLine())
                .seatType(seat.getSeatType())
                .creationDate(seat.getCreationDate().toString())
                .endAvailabilityDate(seat.getEndAvailabilityDate().toString())
                .storey(convertToStoreyDTO(seat.getStorey()))
                .build();
    }

    public static Seat convertToSeatEntity(SeatDTO seatDTO) {
        Seat seat = new Seat();
        seat.setId(seatDTO.getId());
        seat.setCol(seatDTO.getCol());
        seat.setLine(seatDTO.getLine());
        seat.setSeatType(seatDTO.getSeatType());
        seat.setCreationDate(Instant.parse(seatDTO.getCreationDate()));
        seat.setEndAvailabilityDate(Instant.parse(seatDTO.getEndAvailabilityDate()));
        seat.setStorey(convertToStoreyEntity(seatDTO.getStorey()));
        return seat;
    }

    public static List<SeatDTO> convertToSeatDTOList(List<Seat> seats) {
        return seats.stream()
                .map(EntityDTOConverter::convertToSeatDTO)
                .toList();
    }

    public static StoreyDTO convertToStoreyDTO(Storey storey) {
        return StoreyDTO.builder()
                .id(storey.getId())
                .name(storey.getName())
                .build();
    }

    public static Storey convertToStoreyEntity(StoreyDTO storeyDTO) {
        Storey storey = new Storey();
        storey.setId(storeyDTO.getId());
        storey.setName(storeyDTO.getName());
        return storey;
    }
}