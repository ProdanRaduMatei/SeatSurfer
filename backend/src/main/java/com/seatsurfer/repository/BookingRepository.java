package com.seatsurfer.repository;

import com.seatsurfer.entity.Booking;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBySeatIdInAndBookingDate(Collection<Long> seatIds, LocalDate bookingDate);

    @Query("""
        select b
        from Booking b
        join fetch b.seat s
        join fetch s.floor
        join fetch s.seatType
        where b.id = :id
        """)
    Optional<Booking> findDetailedById(@Param("id") Long id);

    boolean existsByBookedByNormalizedNameAndBookingDate(String bookedByNormalizedName, LocalDate bookingDate);

    List<Booking> findBySeatIdInAndBookingDateGreaterThanEqual(Collection<Long> seatIds, LocalDate bookingDate);

    @Query("""
        select b
        from Booking b
        join fetch b.seat s
        join fetch s.floor
        join fetch s.seatType
        where b.bookingDate between :from and :to
        """)
    List<Booking> findDetailedByBookingDateBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
