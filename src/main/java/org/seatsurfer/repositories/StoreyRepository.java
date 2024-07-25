package org.seatsurfer.repositories;

import org.seatsurfer.domain.Storeys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreyRepository extends JpaRepository<Storeys, Long> {
}
