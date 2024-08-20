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
        name = "admins",
        uniqueConstraints = {
            @UniqueConstraint(
                name = "user_name_unique",
                columnNames = "name"
            ),
            @UniqueConstraint(
                name = "user_email_unique",
                columnNames = "email"
            ),
            @UniqueConstraint(
                name = "user_password_unique",
                columnNames = "password"
            )
        }
)
public class Admin {
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
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;

    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String password;

    @OneToMany(
            mappedBy = "admin",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Building> buildings = new ArrayList<>();

    // Add Building to Admin
    public void addBuilding(Building building) {
        buildings.add(building);
        building.setAdmin(this);
    }

    // Remove Building from Admin
    public void removeBuilding(Building building) {
        buildings.remove(building);
        building.setAdmin(null);
    }
}