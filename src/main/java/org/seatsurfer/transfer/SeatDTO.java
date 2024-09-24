package org.seatsurfer.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SeatDTO {
    private Long id;
    private Integer col;
    private Integer line;
    private List<BookingDTO> bookings;
    private String seatType;
    private String creationDate;
    private String endAvailabilityDate;
    private StoreyDTO storey;

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate.toString();
    }

    public void setEndAvailabilityDate(Instant endAvailabilityDate) {
        this.endAvailabilityDate = endAvailabilityDate.toString();
    }

    public Instant getCreationDate(String creationDate) {
        return Instant.parse(creationDate);
    }

    public Instant getEndAvailabilityDate(String endAvailabilityDate) {
        return Instant.parse(endAvailabilityDate);
    }

    public void addBooking(BookingDTO booking) {
        bookings.add(booking);
    }

    public void removeBooking(BookingDTO booking) {
        bookings.remove(booking);
    }
}
