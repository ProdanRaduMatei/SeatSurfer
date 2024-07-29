package org.seatsurfer.controller;

import org.seatsurfer.domain.Storeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.seatsurfer.services.StoreysServices;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import java.util.List;

@RestController
@RequestMapping("api/storeys")
public class StoreysController {
    @Autowired
    private StoreysServices storeysServices;

    @GetMapping
    public List<Storeys> getAllStoreys() {
        return storeysServices.getAllStoreys();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Storeys> getStoreyById(@PathVariable Long id) {
        Optional<Storeys> storey = storeysServices.getStoreyById(id);
        return storey.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Storeys createStorey(@RequestBody Storeys storey) {
        return storeysServices.createStorey(storey);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Storeys> updateStorey(@PathVariable Long id, @RequestBody Storeys storeyDetails) {
        Storeys storey = storeysServices.updateStorey(id, storeyDetails);
        return ResponseEntity.ok(storey);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorey(@PathVariable Long id) {
        storeysServices.deleteStorey(id);
        return ResponseEntity.noContent().build();
    }
}
