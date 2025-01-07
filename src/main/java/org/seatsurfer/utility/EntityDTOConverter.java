package org.seatsurfer.utility;

import org.seatsurfer.domain.Booking;
import org.seatsurfer.domain.Seat;
import org.seatsurfer.domain.Storey;
import org.seatsurfer.domain.User;
import org.seatsurfer.transfer.BookingDTO;
import org.seatsurfer.transfer.SeatDTO;
import org.seatsurfer.transfer.StoreyDTO;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.List;

public class EntityDTOConverter {

    public static BookingDTO convertToBookingDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setDate(booking.getDate().toString());

        // Acum booking.user e un obiect user
        if (booking.getUser() != null) {
            dto.setUserName(booking.getUser().getName());
            dto.setEmail(booking.getUser().getEmail());
        }

        // seat, etc.
        return dto;
    }

    public static Booking convertToBookingEntity(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setId(bookingDTO.getId());

        // În loc de booking.setUserName(...) și booking.setEmail(...):
        // Creăm un user pe care-l atașăm la booking (dacă așa e noua logică):
        if (bookingDTO.getUserName() != null || bookingDTO.getEmail() != null) {
            User user = new User();
            user.setName(bookingDTO.getUserName());   // userName provenit din DTO
            user.setEmail(bookingDTO.getEmail());     // email provenit din DTO
            booking.setUser(user);
        }

        // Dacă BookingDTO nu mai are .getSeat(), verifică cum se numește câmpul
        // Poate e seatDTO, poate e altceva. De ex.:
        if (bookingDTO.getSeatDTO() != null) {
            booking.setSeat(convertToSeatEntity(bookingDTO.getSeatDTO()));
        }

        // Parsează data doar dacă există un string valid
        if (bookingDTO.getDate() != null) {
            booking.setDate(Instant.parse(bookingDTO.getDate()));
        }

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