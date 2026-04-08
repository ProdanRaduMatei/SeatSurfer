package com.seatsurfer.service;

import com.seatsurfer.dto.FloorDtos;
import com.seatsurfer.dto.ReportDtos;
import com.seatsurfer.entity.Booking;
import com.seatsurfer.entity.BookingAuditEvent;
import com.seatsurfer.entity.Floor;
import com.seatsurfer.entity.Seat;
import com.seatsurfer.model.BookingEventType;
import com.seatsurfer.repository.BookingAuditEventRepository;
import com.seatsurfer.repository.BookingRepository;
import com.seatsurfer.repository.FloorRepository;
import com.seatsurfer.repository.SeatRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final FloorRepository floorRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final BookingAuditEventRepository bookingAuditEventRepository;

    public ReportService(
        FloorRepository floorRepository,
        SeatRepository seatRepository,
        BookingRepository bookingRepository,
        BookingAuditEventRepository bookingAuditEventRepository
    ) {
        this.floorRepository = floorRepository;
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
        this.bookingAuditEventRepository = bookingAuditEventRepository;
    }

    public ReportDtos.DailyReportResponse getDailyReport(LocalDate date) {
        List<FloorDtos.FloorSummary> summaries = floorRepository.findByActiveTrueOrderBySortOrderAscNameAsc().stream()
            .map(floor -> summarizeFloor(floor, date))
            .toList();

        int totalSeats = summaries.stream().mapToInt(FloorDtos.FloorSummary::totalSeats).sum();
        int bookedSeats = summaries.stream().mapToInt(FloorDtos.FloorSummary::bookedSeats).sum();
        int availableSeats = summaries.stream().mapToInt(FloorDtos.FloorSummary::availableSeats).sum();

        return new ReportDtos.DailyReportResponse(
            date,
            summaries,
            new ReportDtos.Totals(totalSeats, bookedSeats, availableSeats, percentage(bookedSeats, totalSeats))
        );
    }

    public ReportDtos.LoadReportResponse getLoadReport(LocalDate from, LocalDate to) {
        List<ReportDtos.FloorLoadReport> floorReports = floorRepository.findByActiveTrueOrderBySortOrderAscNameAsc().stream()
            .map(floor -> buildFloorLoadReport(floor, from, to))
            .toList();
        return new ReportDtos.LoadReportResponse(from, to, floorReports);
    }

    public ReportDtos.MonthlyReportResponse getMonthlyReport(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate from = yearMonth.atDay(1);
        LocalDate to = yearMonth.atEndOfMonth();
        List<BookingAuditEvent> events = bookingAuditEventRepository.findByBookingDateBetween(from, to);
        int booked = (int) events.stream().filter(event -> event.getEventType() == BookingEventType.BOOKED).count();
        int cancelled = (int) events.stream().filter(event -> event.getEventType() != BookingEventType.BOOKED).count();
        return new ReportDtos.MonthlyReportResponse(
            year,
            month,
            booked,
            cancelled,
            getLoadReport(from, to).floors()
        );
    }

    public FloorDtos.FloorSummary summarizeFloor(Floor floor, LocalDate date) {
        List<Seat> seats = seatRepository.findActiveLayoutForFloor(floor.getId(), date);
        List<Long> seatIds = seats.stream().map(Seat::getId).toList();
        List<Booking> bookings = seatIds.isEmpty() ? List.of() : bookingRepository.findBySeatIdInAndBookingDate(seatIds, date);
        int totalSeats = seats.size();
        int bookedSeats = bookings.size();
        int availableSeats = Math.max(totalSeats - bookedSeats, 0);
        return new FloorDtos.FloorSummary(
            floor.getId(),
            floor.getName(),
            floor.getDescription(),
            floor.getSortOrder(),
            totalSeats,
            bookedSeats,
            availableSeats,
            percentage(bookedSeats, totalSeats)
        );
    }

    private ReportDtos.FloorLoadReport buildFloorLoadReport(Floor floor, LocalDate from, LocalDate to) {
        List<ReportDtos.DailyLoadPoint> dailyPoints = new ArrayList<>();
        LocalDate cursor = from;
        double totalLoad = 0.0;
        double peakLoad = 0.0;
        int bookingVolume = 0;

        while (!cursor.isAfter(to)) {
            FloorDtos.FloorSummary summary = summarizeFloor(floor, cursor);
            dailyPoints.add(new ReportDtos.DailyLoadPoint(
                cursor,
                summary.totalSeats(),
                summary.bookedSeats(),
                summary.availableSeats(),
                summary.loadPercentage()
            ));
            totalLoad += summary.loadPercentage();
            peakLoad = Math.max(peakLoad, summary.loadPercentage());
            bookingVolume += summary.bookedSeats();
            cursor = cursor.plusDays(1);
        }

        double averageLoad = dailyPoints.isEmpty() ? 0.0 : totalLoad / dailyPoints.size();
        return new ReportDtos.FloorLoadReport(
            floor.getId(),
            floor.getName(),
            round(averageLoad),
            round(peakLoad),
            bookingVolume,
            dailyPoints
        );
    }

    private double percentage(int booked, int total) {
        if (total == 0) {
            return 0.0;
        }
        return round((booked * 100.0) / total);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
