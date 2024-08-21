package org.seatsurfer.web;

import org.seatsurfer.domain.Building;
import org.seatsurfer.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/buildings")
public class BuildingController {
    @Autowired
    private BuildingService buildingService;

    @GetMapping
    public List<Building> getAllBuildings() {
        return buildingService.getAllBuildings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable Long id) {
        Optional<Building> building = buildingService.getBuildingById(id);
        return building.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Building createBuilding(@RequestBody Building building) {
        return buildingService.createBuilding(building);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Building> updateBuilding(@PathVariable Long id, @RequestBody Building buildingDetails) {
        Building building = buildingService.updateBuilding(id, buildingDetails);
        return ResponseEntity.ok(building);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        buildingService.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }
}
