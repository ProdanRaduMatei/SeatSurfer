package org.seatsurfer.service;

import org.seatsurfer.domain.Building;
import org.seatsurfer.persistence.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuildingService {
    @Autowired
    private BuildingRepository buildingRepository;

    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    public Optional<Building> getBuildingById(Long id) {
        return buildingRepository.findById(id);
    }

    public Building createBuilding(Building building) {
        return buildingRepository.save(building);
    }

    public void deleteBuilding(Long id) {
        buildingRepository.deleteById(id);
    }
}
