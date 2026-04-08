package com.seatsurfer.repository;

import com.seatsurfer.entity.Seat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("""
        select s
        from Seat s
        join fetch s.seatType
        where s.floor.id = :floorId
          and s.effectiveFrom <= :date
          and (s.effectiveTo is null or s.effectiveTo >= :date)
        order by s.rowIndex asc, s.columnIndex asc
        """)
    List<Seat> findActiveLayoutForFloor(@Param("floorId") Long floorId, @Param("date") LocalDate date);

    @Query("""
        select s
        from Seat s
        join fetch s.seatType
        where s.floor.id = :floorId
          and (s.effectiveTo is null or s.effectiveTo >= :effectiveFrom)
        order by s.rowIndex asc, s.columnIndex asc, s.effectiveFrom asc
        """)
    List<Seat> findOverlappingFromDate(@Param("floorId") Long floorId, @Param("effectiveFrom") LocalDate effectiveFrom);

    @Query("""
        select s
        from Seat s
        join fetch s.floor
        join fetch s.seatType
        where s.id in :ids
        """)
    List<Seat> findDetailedByIds(@Param("ids") Collection<Long> ids);
}
