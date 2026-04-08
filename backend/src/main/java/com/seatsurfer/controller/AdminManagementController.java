package com.seatsurfer.controller;

import com.seatsurfer.dto.FloorDtos;
import com.seatsurfer.service.FloorService;
import com.seatsurfer.service.SeatTypeService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminManagementController {

    private final FloorService floorService;
    private final SeatTypeService seatTypeService;

    public AdminManagementController(FloorService floorService, SeatTypeService seatTypeService) {
        this.floorService = floorService;
        this.seatTypeService = seatTypeService;
    }

    @GetMapping("/floors")
    public ResponseEntity<List<FloorDtos.FloorResponse>> listFloors() {
        return ResponseEntity.ok(floorService.listAdminFloors());
    }

    @PostMapping("/floors")
    public ResponseEntity<FloorDtos.FloorResponse> createFloor(@Valid @RequestBody FloorDtos.FloorRequest request) {
        return ResponseEntity.ok(floorService.createFloor(request));
    }

    @PutMapping("/floors/{floorId}")
    public ResponseEntity<FloorDtos.FloorResponse> updateFloor(
        @PathVariable Long floorId,
        @Valid @RequestBody FloorDtos.FloorRequest request
    ) {
        return ResponseEntity.ok(floorService.updateFloor(floorId, request));
    }

    @DeleteMapping("/floors/{floorId}")
    public ResponseEntity<Void> deactivateFloor(@PathVariable Long floorId) {
        floorService.deactivateFloor(floorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/seat-types")
    public ResponseEntity<List<FloorDtos.SeatTypeResponse>> listSeatTypes() {
        return ResponseEntity.ok(seatTypeService.listAll());
    }

    @PostMapping("/seat-types")
    public ResponseEntity<FloorDtos.SeatTypeResponse> createSeatType(@Valid @RequestBody FloorDtos.SeatTypeRequest request) {
        return ResponseEntity.ok(seatTypeService.create(request));
    }

    @PutMapping("/seat-types/{seatTypeId}")
    public ResponseEntity<FloorDtos.SeatTypeResponse> updateSeatType(
        @PathVariable Long seatTypeId,
        @Valid @RequestBody FloorDtos.SeatTypeRequest request
    ) {
        return ResponseEntity.ok(seatTypeService.update(seatTypeId, request));
    }

    @DeleteMapping("/seat-types/{seatTypeId}")
    public ResponseEntity<Void> deactivateSeatType(@PathVariable Long seatTypeId) {
        seatTypeService.deactivate(seatTypeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/floors/{floorId}/layout")
    public ResponseEntity<FloorDtos.FloorLayoutResponse> getLayout(
        @PathVariable Long floorId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(floorService.getLayout(floorId, date, null));
    }

    @PutMapping("/floors/{floorId}/layout")
    public ResponseEntity<FloorDtos.FloorLayoutResponse> replaceLayout(
        @PathVariable Long floorId,
        @Valid @RequestBody FloorDtos.LayoutRequest request
    ) {
        return ResponseEntity.ok(floorService.replaceLayout(floorId, request));
    }
}
