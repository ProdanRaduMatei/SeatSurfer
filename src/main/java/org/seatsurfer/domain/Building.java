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
        name = "buildings",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "building_id_unique",
                    columnNames = "id"
            ),
            @UniqueConstraint(
                name = "building_name_unique",
                columnNames = "name"
            )
        }
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

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "created_by",
            nullable = false
    )
    private Long createdBy;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(
            name = "storey_id",
            referencedColumnName = "id"
    )
    private List<Storey> storeys = new ArrayList<>();
}