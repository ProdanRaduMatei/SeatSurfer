package org.seatsurfer.repositories;

import org.seatsurfer.domain.Seats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seats, Long> {
}
