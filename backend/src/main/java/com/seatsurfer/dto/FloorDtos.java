package com.seatsurfer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public final class FloorDtos {

    private FloorDtos() {
    }

    public record FloorRequest(
        @NotBlank(message = "Floor name is required")
        String name,
        String description,
        Integer sortOrder,
        Boolean active
    ) {
    }

    public record FloorResponse(
        Long id,
        String name,
        String description,
        int sortOrder,
        boolean active
    ) {
    }

    public record SeatTypeRequest(
        @NotBlank(message = "Seat type code is required")
        String code,
        @NotBlank(message = "Seat type name is required")
        String name,
        String description,
        @NotBlank(message = "Seat color is required")
        String colorHex,
        Boolean hasMonitors,
        Boolean standing,
        Boolean requiresChair,
        Boolean active
    ) {
    }

    public record SeatTypeResponse(
        Long id,
        String code,
        String name,
        String description,
        String colorHex,
        boolean hasMonitors,
        boolean standing,
        boolean requiresChair,
        boolean active
    ) {
    }

    public record LayoutRequest(
        @NotNull(message = "Effective from date is required")
        LocalDate effectiveFrom,
        @NotNull(message = "Row count is required")
        @Min(value = 1, message = "At least one row is required")
        @Max(value = 30, message = "No more than 30 rows are supported")
        Integer rows,
        @NotNull(message = "Column count is required")
        @Min(value = 1, message = "At least one column is required")
        @Max(value = 30, message = "No more than 30 columns are supported")
        Integer columns,
        @NotEmpty(message = "At least one seat must be configured")
        List<@Valid LayoutCellRequest> seats
    ) {
    }

    public record LayoutCellRequest(
        @NotNull(message = "Row is required")
        @Min(value = 1, message = "Row must be 1 or greater")
        Integer rowIndex,
        @NotNull(message = "Column is required")
        @Min(value = 1, message = "Column must be 1 or greater")
        Integer columnIndex,
        @NotNull(message = "Seat type is required")
        Long seatTypeId
    ) {
    }

    public record FloorSummary(
        Long id,
        String name,
        String description,
        int sortOrder,
        int totalSeats,
        int bookedSeats,
        int availableSeats,
        double loadPercentage
    ) {
    }

    public record SeatView(
        Long seatId,
        int rowIndex,
        int columnIndex,
        String label,
        Long seatTypeId,
        String seatTypeName,
        String seatColorHex,
        boolean standing,
        boolean hasMonitors,
        boolean requiresChair,
        boolean booked,
        String bookedByName,
        Long bookingId,
        boolean canBook,
        boolean canCancel
    ) {
    }

    public record FloorLayoutResponse(
        FloorSummary floor,
        LocalDate date,
        int rows,
        int columns,
        List<SeatTypeResponse> legend,
        List<SeatView> seats
    ) {
    }
}
