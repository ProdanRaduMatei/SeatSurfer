package org.seatsurfer.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class StoreyDTO {
    private Long id;
    private String name;
    private Integer maxRows;
    private Integer maxCols;
    private List<SeatDTO> seats;

    public void addSeat(SeatDTO seat) {
        seats.add(seat);
    }

    public void removeSeat(SeatDTO seat) {
        seats.remove(seat);
    }
}
