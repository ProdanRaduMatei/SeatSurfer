package org.seatsurfer.persistence;

import org.seatsurfer.domain.Storey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreyRepository extends JpaRepository<Storey, Long> {
}
