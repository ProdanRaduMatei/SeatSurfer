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
@Table(
        name = "storeys",
        uniqueConstraints = {
            @UniqueConstraint(
                name = "storey_id_unique",
                columnNames = "id"
            ),
            @UniqueConstraint(
                name = "storey_name_unique",
                columnNames = "name"
            )
        }
)
public class Storey {
    @Id
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "max_rows",
            nullable = false
    )
    final private Integer maxRows = 25;

    @Column(
            name = "max_cols",
            nullable = false
    )
    final private Integer maxCols = 25;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    private Building building;

    @OneToMany(
            mappedBy = "storey",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Seat> seats = new ArrayList<>();

    // Add Seat to Storey
    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setStorey(this);
    }

    // Remove Seat from Storey
    public void removeSeat(Seat seat) {
        seats.remove(seat);
        seat.setStorey(null);
    }
}
