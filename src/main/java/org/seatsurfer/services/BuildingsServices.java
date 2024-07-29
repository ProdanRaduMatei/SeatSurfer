package org.seatsurfer.services;

import org.seatsurfer.domain.Buildings;
import org.seatsurfer.repositories.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuildingsServices {
    @Autowired
    private BuildingRepository buildingRepository;

    public List<Buildings> getAllBuildings() {
        return buildingRepository.findAll();
    }

    public Optional<Buildings> getBuildingById(Long id) {
        return buildingRepository.findById(id);
    }

    public Buildings createBuilding(Buildings building) {
        return buildingRepository.save(building);
    }

    public void deleteBuilding(Long id) {
        buildingRepository.deleteById(id);
    }
}
