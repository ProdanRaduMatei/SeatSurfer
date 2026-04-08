package com.seatsurfer.service;

import com.seatsurfer.dto.FloorDtos;
import com.seatsurfer.entity.SeatType;
import com.seatsurfer.repository.SeatTypeRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SeatTypeService {

    private final SeatTypeRepository seatTypeRepository;

    public SeatTypeService(SeatTypeRepository seatTypeRepository) {
        this.seatTypeRepository = seatTypeRepository;
    }

    public List<FloorDtos.SeatTypeResponse> listAll() {
        return seatTypeRepository.findAll().stream()
            .sorted(Comparator.comparing(SeatType::getName))
            .map(this::toResponse)
            .toList();
    }

    public List<FloorDtos.SeatTypeResponse> listActive() {
        return seatTypeRepository.findByActiveTrueOrderByNameAsc().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public FloorDtos.SeatTypeResponse create(FloorDtos.SeatTypeRequest request) {
        SeatType seatType = new SeatType();
        apply(seatType, request);
        return toResponse(seatTypeRepository.save(seatType));
    }

    @Transactional
    public FloorDtos.SeatTypeResponse update(Long id, FloorDtos.SeatTypeRequest request) {
        SeatType seatType = seatTypeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat type not found"));
        apply(seatType, request);
        return toResponse(seatType);
    }

    @Transactional
    public void deactivate(Long id) {
        SeatType seatType = seatTypeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat type not found"));
        seatType.setActive(false);
    }

    public FloorDtos.SeatTypeResponse toResponse(SeatType seatType) {
        return new FloorDtos.SeatTypeResponse(
            seatType.getId(),
            seatType.getCode(),
            seatType.getName(),
            seatType.getDescription(),
            seatType.getColorHex(),
            seatType.isHasMonitors(),
            seatType.isStanding(),
            seatType.isRequiresChair(),
            seatType.isActive()
        );
    }

    private void apply(SeatType seatType, FloorDtos.SeatTypeRequest request) {
        seatType.setCode(request.code().trim().toUpperCase());
        seatType.setName(request.name().trim());
        seatType.setDescription(request.description() == null ? null : request.description().trim());
        seatType.setColorHex(request.colorHex().trim());
        seatType.setHasMonitors(Boolean.TRUE.equals(request.hasMonitors()));
        seatType.setStanding(Boolean.TRUE.equals(request.standing()));
        seatType.setRequiresChair(request.requiresChair() == null || request.requiresChair());
        seatType.setActive(request.active() == null || request.active());
    }
}
