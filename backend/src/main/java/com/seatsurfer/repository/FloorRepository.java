package com.seatsurfer.repository;

import com.seatsurfer.entity.Floor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorRepository extends JpaRepository<Floor, Long> {

    List<Floor> findByActiveTrueOrderBySortOrderAscNameAsc();
}
