package org.seatsurfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.seatsurfer.persistence.StoreyRepository;
import org.seatsurfer.domain.Storey;

import java.util.List;
import java.util.Optional;

@Service
public class StoreyService {
    @Autowired
    private StoreyRepository storeyRepository;

    public List<Storey> getAllStoreys() {
        return storeyRepository.findAll();
    }

    public Optional<Storey> getStoreyById(Long id) {
        return storeyRepository.findById(id);
    }

    public Storey createStorey(Storey storey) {
        return storeyRepository.save(storey);
    }

    public Storey updateStorey(Long id, Storey storeyDetails) {
        Storey storey = storeyRepository.findById(id).orElseThrow();
        storey.setName(storeyDetails.getName());
        storey.setBuildingId(storeyDetails.getBuildingId());
        storey.setSeats(storeyDetails.getSeats());
        return storeyRepository.save(storey);
    }

    public void deleteStorey(Long id) {
        storeyRepository.deleteById(id);
    }
}
