package org.seatsurfer.web;

import org.seatsurfer.domain.Storey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.seatsurfer.service.StoreyService;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import java.util.List;

@RestController
@RequestMapping("api/storeys")
public class StoreyController {
    @Autowired
    private StoreyService storeyService;

    @GetMapping
    public List<Storey> getAllStoreys() {
        return storeyService.getAllStoreys();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Storey> getStoreyById(@PathVariable Long id) {
        Optional<Storey> storey = storeyService.getStoreyById(id);
        return storey.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Storey createStorey(@RequestBody Storey storey) {
        return storeyService.createStorey(storey);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Storey> updateStorey(@PathVariable Long id, @RequestBody Storey storeyDetails) {
        Storey storey = storeyService.updateStorey(id, storeyDetails);
        return ResponseEntity.ok(storey);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorey(@PathVariable Long id) {
        storeyService.deleteStorey(id);
        return ResponseEntity.noContent().build();
    }
}
