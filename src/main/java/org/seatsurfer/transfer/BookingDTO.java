package org.seatsurfer.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {
    private Long id;
    private String date;
    private boolean confirmed;
    private String userName;
    private String email;
    private SeatDTO seatDTO;

    // sau, dacă preferi un sub-DTO:
    // private UserDTO user;
    public Instant getBookingDateConverted(String date) {
        return Instant.parse(date);
    }

    public void setBookingDate(Instant date) {
        this.date = date.toString();
    }

    public void setBookingDate(String date) {
        this.date = date;
    }

    public Instant getBookingDate() {
        return Instant.parse(date);
    }

    public SeatDTO getSeatDTO() { return seatDTO; }
}
