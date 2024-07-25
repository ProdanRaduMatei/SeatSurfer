package org.seatsurfer.domain;

import jakarta.persistence.*;

@Entity(name = "Buildings")
@Table(
        name = "buildings"
)
public class Buildings {
    @Id
    @SequenceGenerator(
            name = "building_sequence",
            sequenceName = "building_sequence",
            allocationSize = 1
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    public Buildings() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Buildings{" +
                "id=" + id +
                '}';
    }
}
