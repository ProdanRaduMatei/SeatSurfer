package org.seatsurfer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.seatsurfer.repositories.StoreyRepository;
import org.seatsurfer.domain.Storeys;

import java.util.List;
import java.util.Optional;

@Service
public class StoreysServices {
    @Autowired
    private StoreyRepository storeyRepository;

    public List<Storeys> getAllStoreys() {
        return storeyRepository.findAll();
    }

    public Optional<Storeys> getStoreyById(Long id) {
        return storeyRepository.findById(id);
    }

    public Storeys createStorey(Storeys storey) {
        return storeyRepository.save(storey);
    }

    public Storeys updateStorey(Long id, Storeys storeyDetails) {
        Storeys storey = storeyRepository.findById(id).orElseThrow();
        storey.setBuildingId(storeyDetails.getBuildingId());
        return storeyRepository.save(storey);
    }

    public void deleteStorey(Long id) {
        storeyRepository.deleteById(id);
    }
}
