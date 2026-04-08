package com.seatsurfer.repository;

import com.seatsurfer.entity.SeatType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatTypeRepository extends JpaRepository<SeatType, Long> {

    List<SeatType> findByActiveTrueOrderByNameAsc();
}
