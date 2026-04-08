package com.seatsurfer.service;

import com.seatsurfer.dto.FloorDtos;
import com.seatsurfer.entity.Booking;
import com.seatsurfer.entity.Floor;
import com.seatsurfer.entity.Seat;
import com.seatsurfer.entity.SeatType;
import com.seatsurfer.repository.BookingRepository;
import com.seatsurfer.repository.FloorRepository;
import com.seatsurfer.repository.SeatRepository;
import com.seatsurfer.repository.SeatTypeRepository;
import com.seatsurfer.util.NameUtils;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FloorService {

    private final FloorRepository floorRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final ReportService reportService;
    private final SeatTypeService seatTypeService;
    private final BookingService bookingService;

    public FloorService(
        FloorRepository floorRepository,
        SeatTypeRepository seatTypeRepository,
        SeatRepository seatRepository,
        BookingRepository bookingRepository,
        ReportService reportService,
        SeatTypeService seatTypeService,
        BookingService bookingService
    ) {
        this.floorRepository = floorRepository;
        this.seatTypeRepository = seatTypeRepository;
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
        this.reportService = reportService;
        this.seatTypeService = seatTypeService;
        this.bookingService = bookingService;
    }

    public List<FloorDtos.FloorResponse> listAdminFloors() {
        return floorRepository.findAll().stream()
            .sorted((left, right) -> {
                int bySort = Integer.compare(left.getSortOrder(), right.getSortOrder());
                return bySort != 0 ? bySort : left.getName().compareToIgnoreCase(right.getName());
            })
            .map(this::toResponse)
            .toList();
    }

    public List<FloorDtos.FloorSummary> listPublicSummaries(LocalDate date) {
        return floorRepository.findByActiveTrueOrderBySortOrderAscNameAsc().stream()
            .map(floor -> reportService.summarizeFloor(floor, date))
            .toList();
    }

    @Transactional
    public FloorDtos.FloorResponse createFloor(FloorDtos.FloorRequest request) {
        Floor floor = new Floor();
        applyFloor(floor, request);
        return toResponse(floorRepository.save(floor));
    }

    @Transactional
    public FloorDtos.FloorResponse updateFloor(Long id, FloorDtos.FloorRequest request) {
        Floor floor = floorRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Floor not found"));
        applyFloor(floor, request);
        return toResponse(floor);
    }

    @Transactional
    public void deactivateFloor(Long id) {
        Floor floor = floorRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Floor not found"));
        floor.setActive(false);
    }

    public FloorDtos.FloorLayoutResponse getLayout(Long floorId, LocalDate date, String viewerName) {
        Floor floor = floorRepository.findById(floorId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Floor not found"));

        List<Seat> seats = seatRepository.findActiveLayoutForFloor(floorId, date);
        List<Long> seatIds = seats.stream().map(Seat::getId).toList();
        List<Booking> bookings = seatIds.isEmpty() ? List.of() : bookingRepository.findBySeatIdInAndBookingDate(seatIds, date);
        Map<Long, Booking> bookingBySeatId = new HashMap<>();
        for (Booking booking : bookings) {
            bookingBySeatId.put(booking.getSeat().getId(), booking);
        }

        String normalizedViewerName = NameUtils.normalizePersonName(viewerName);
        int rows = seats.stream().mapToInt(Seat::getRowIndex).max().orElse(1);
        int columns = seats.stream().mapToInt(Seat::getColumnIndex).max().orElse(1);

        List<FloorDtos.SeatView> seatViews = seats.stream().map(seat -> {
            Booking booking = bookingBySeatId.get(seat.getId());
            boolean booked = booking != null;
            boolean canCancel = booked && booking.getBookedByNormalizedName().equals(normalizedViewerName);
            return new FloorDtos.SeatView(
                seat.getId(),
                seat.getRowIndex(),
                seat.getColumnIndex(),
                seat.getLabel(),
                seat.getSeatType().getId(),
                seat.getSeatType().getName(),
                seat.getSeatType().getColorHex(),
                seat.getSeatType().isStanding(),
                seat.getSeatType().isHasMonitors(),
                seat.getSeatType().isRequiresChair(),
                booked,
                booked ? booking.getBookedByName() : null,
                booked ? booking.getId() : null,
                !booked,
                canCancel
            );
        }).toList();

        return new FloorDtos.FloorLayoutResponse(
            reportService.summarizeFloor(floor, date),
            date,
            rows,
            columns,
            seatTypeService.listActive(),
            seatViews
        );
    }

    @Transactional
    public FloorDtos.FloorLayoutResponse replaceLayout(Long floorId, FloorDtos.LayoutRequest request) {
        Floor floor = floorRepository.findById(floorId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Floor not found"));

        if (request.effectiveFrom().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Layout changes must start today or in the future");
        }

        Set<String> occupiedCells = new HashSet<>();
        for (FloorDtos.LayoutCellRequest seat : request.seats()) {
            if (seat.rowIndex() > request.rows() || seat.columnIndex() > request.columns()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A configured seat sits outside the selected grid");
            }
            String key = seat.rowIndex() + ":" + seat.columnIndex();
            if (!occupiedCells.add(key)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate seat coordinates are not allowed");
            }
        }

        List<Seat> overlappingSeats = seatRepository.findOverlappingFromDate(floorId, request.effectiveFrom());
        List<Long> seatIds = overlappingSeats.stream().map(Seat::getId).toList();
        bookingService.cancelBookingsForReconfiguration(seatIds, request.effectiveFrom());

        LocalDate retireAt = request.effectiveFrom().minusDays(1);
        for (Seat seat : overlappingSeats) {
            seat.setEffectiveTo(retireAt);
        }

        Map<Long, SeatType> seatTypeMap = new HashMap<>();
        seatTypeRepository.findAllById(request.seats().stream().map(FloorDtos.LayoutCellRequest::seatTypeId).toList())
            .forEach(seatType -> seatTypeMap.put(seatType.getId(), seatType));

        for (FloorDtos.LayoutCellRequest cell : request.seats()) {
            SeatType seatType = seatTypeMap.get(cell.seatTypeId());
            if (seatType == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more selected seat types do not exist");
            }
            Seat seat = new Seat();
            seat.setFloor(floor);
            seat.setSeatType(seatType);
            seat.setRowIndex(cell.rowIndex());
            seat.setColumnIndex(cell.columnIndex());
            seat.setLabel(NameUtils.seatLabel(cell.rowIndex(), cell.columnIndex()));
            seat.setEffectiveFrom(request.effectiveFrom());
            seatRepository.save(seat);
        }

        return getLayout(floorId, request.effectiveFrom(), null);
    }

    private FloorDtos.FloorResponse toResponse(Floor floor) {
        return new FloorDtos.FloorResponse(
            floor.getId(),
            floor.getName(),
            floor.getDescription(),
            floor.getSortOrder(),
            floor.isActive()
        );
    }

    private void applyFloor(Floor floor, FloorDtos.FloorRequest request) {
        floor.setName(request.name().trim());
        floor.setDescription(request.description() == null ? null : request.description().trim());
        floor.setSortOrder(request.sortOrder() == null ? 0 : request.sortOrder());
        floor.setActive(request.active() == null || request.active());
    }
}
