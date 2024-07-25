package org.seatsurfer.domain;

import jakarta.persistence.*;

@Entity(name = "Storeys")
@Table(
        name = "storeys"
)
public class Storeys {
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
            name = "building_id",
            nullable = false
    )
    private Long buildingId;

    public Storeys() {
    }

    public Storeys(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public String toString() {
        return "Storeys{" +
                "id=" + id +
                ", buildingId=" + buildingId +
                '}';
    }
}
