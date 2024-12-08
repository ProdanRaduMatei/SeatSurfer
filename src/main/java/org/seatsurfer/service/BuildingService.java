package org.seatsurfer.service;

import org.seatsurfer.domain.Building;
import org.seatsurfer.persistence.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuildingService {
    private final BuildingRepository buildingRepository;

    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    public Optional<Building> getBuildingById(Long id) {
        return buildingRepository.findById(id);
    }

    public Building createBuilding(Building building) {
        return buildingRepository.save(building);
    }

    public Building updateBuilding(Long id, Building buildingDetails) {
        Building building = buildingRepository.findById(id).orElseThrow();
        building.setName(buildingDetails.getName());
        building.setAdmin(buildingDetails.getAdmin());
        return buildingRepository.save(building);
    }

    public void deleteBuilding(Long id) {
        buildingRepository.deleteById(id);
    }
}
