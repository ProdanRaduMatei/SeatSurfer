package org.seatsurfer.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Storeys")
@Table(
        name = "storeys"
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;

    @OneToMany(
            mappedBy = "storey",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Seat> seats = new ArrayList<>();
}
