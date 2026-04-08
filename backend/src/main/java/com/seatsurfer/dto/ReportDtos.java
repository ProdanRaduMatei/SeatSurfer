package com.seatsurfer.dto;

import java.time.LocalDate;
import java.util.List;

public final class ReportDtos {

    private ReportDtos() {
    }

    public record Totals(
        int totalSeats,
        int bookedSeats,
        int availableSeats,
        double loadPercentage
    ) {
    }

    public record DailyReportResponse(
        LocalDate date,
        List<FloorDtos.FloorSummary> floors,
        Totals totals
    ) {
    }

    public record DailyLoadPoint(
        LocalDate date,
        int totalSeats,
        int bookedSeats,
        int availableSeats,
        double loadPercentage
    ) {
    }

    public record FloorLoadReport(
        Long floorId,
        String floorName,
        double averageLoadPercentage,
        double peakLoadPercentage,
        int totalBookings,
        List<DailyLoadPoint> dailyPoints
    ) {
    }

    public record LoadReportResponse(
        LocalDate from,
        LocalDate to,
        List<FloorLoadReport> floors
    ) {
    }

    public record MonthlyReportResponse(
        int year,
        int month,
        int totalCompletedBookings,
        int totalCancellations,
        List<FloorLoadReport> floors
    ) {
    }
}
