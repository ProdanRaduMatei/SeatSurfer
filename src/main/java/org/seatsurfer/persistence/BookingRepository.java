package org.seatsurfer.persistence;

import org.seatsurfer.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    //Custom query to find a booking by seat id and date
    Optional<Booking> findBySeatIdAndDate(Long seatId, Instant date);
}
