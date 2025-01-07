package org.seatsurfer.persistence;

import org.seatsurfer.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query("SELECT s FROM Seat s WHERE s.storey.name = :storeyName AND :date BETWEEN s.creationDate AND s.endAvailabilityDate")
    List<Seat> findSeatsByStoreyAndDate(@Param("storeyName") String storeyName, @Param("date") Instant date);
}