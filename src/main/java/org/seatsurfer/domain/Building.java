package org.seatsurfer.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Buildings")
@Table(
        name = "buildings"
)
public class Building {
    @Id
    @SequenceGenerator(
            name = "building_sequence",
            sequenceName = "building_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "building_sequence"
    )
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "storey_id",
            referencedColumnName = "id"
    )
    private List<Storey> storeys = new ArrayList<>();
}