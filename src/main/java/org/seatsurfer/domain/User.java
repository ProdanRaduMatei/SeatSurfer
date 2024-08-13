package org.seatsurfer.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Users")
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_name_unique",
                        columnNames = "name"
                )
        }
)
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "user_sequence",
            strategy = SEQUENCE
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
            name = "is_admin",
            nullable = false
    )
    private Boolean isAdmin;
}