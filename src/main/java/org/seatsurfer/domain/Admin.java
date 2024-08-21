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
@Table(name = "admins")
public class Admin {
    @Id
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Building> buildings = new ArrayList<>();

    public void addBuilding(Building building) {
        buildings.add(building);
        building.setAdmin(this);
    }

    public void removeBuilding(Building building) {
        buildings.remove(building);
        building.setAdmin(null);
    }
}