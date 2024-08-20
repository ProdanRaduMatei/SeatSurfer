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
    @SequenceGenerator(
            name = "storey_sequence",
            sequenceName = "storey_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "storey_sequence",
            strategy = GenerationType.SEQUENCE
    )
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

    @Column(
            name = "building_id",
            nullable = false
    )
    private Long buildingId;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(
            name = "seat_id",
            referencedColumnName = "id"
    )
    private List<Seat> seats = new ArrayList<>();
}
