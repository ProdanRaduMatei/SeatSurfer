package org.seatsurfer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "storeys")
public class Storey {
    @Id
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_rows", nullable = false)
    private final Integer maxRows = 25;

    @Column(name = "max_cols", nullable = false)
    private final Integer maxCols = 25;

    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;

    @OneToMany(mappedBy = "storey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();

    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setStorey(this);
    }

    public void removeSeat(Seat seat) {
        seats.remove(seat);
        seat.setStorey(null);
    }
}
