package org.seatsurfer.controller;

import org.seatsurfer.domain.Buildings;
import org.seatsurfer.services.BuildingsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/buildings")
public class BuildingsController {
    @Autowired
    private BuildingsServices buildingsServices;

    @GetMapping
    public List<Buildings> getAllBuildings() {
        return buildingsServices.getAllBuildings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Buildings> getBuildingById(@PathVariable Long id) {
        Optional<Buildings> building = buildingsServices.getBuildingById(id);
        return building.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Buildings createBuilding(@RequestBody Buildings building) {
        return buildingsServices.createBuilding(building);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        buildingsServices.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }
}
