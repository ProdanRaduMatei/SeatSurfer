package org.seatsurfer.transfer;

import org.seatsurfer.domain.Seat;
import org.seatsurfer.service.SeatService;
import org.seatsurfer.utility.EntityDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.List;

@Component
public class AllSeats {
    @Autowired
    private SeatService seatService;

    public List<SeatDTO> getAllSeats(@RequestParam String storeyName, @RequestParam String date) {
        Instant dateInstant = Instant.parse(date);
        List<Seat> allSeats = seatService.getAllSeats(storeyName, dateInstant);
        return EntityDTOConverter.convertToSeatDTOList(allSeats);
    }
}
