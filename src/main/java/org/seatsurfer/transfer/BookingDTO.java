package org.seatsurfer.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BookingDTO {
    private Long id;
    private String userName;
    private String email;
    private SeatDTO seat;
    private String date;

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
}
