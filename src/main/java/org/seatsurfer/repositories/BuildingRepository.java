package org.seatsurfer.repositories;

import org.seatsurfer.domain.Buildings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Buildings, Long> {
}
