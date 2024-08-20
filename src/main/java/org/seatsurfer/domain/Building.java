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

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    private Admin admin;

    @OneToMany(
            mappedBy = "building",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Storey> storeys = new ArrayList<>();

    // Add Storey to Building
    public void addStorey(Storey storey) {
        storeys.add(storey);
        storey.setBuilding(this);
    }

    // Remove Storey from Building
    public void removeStorey(Storey storey) {
        storeys.remove(storey);
        storey.setBuilding(null);
    }
}