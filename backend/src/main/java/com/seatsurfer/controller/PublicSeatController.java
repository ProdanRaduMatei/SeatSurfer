package com.seatsurfer.controller;

import com.seatsurfer.dto.BookingDtos;
import com.seatsurfer.dto.FloorDtos;
import com.seatsurfer.dto.ReportDtos;
import com.seatsurfer.service.BookingService;
import com.seatsurfer.service.FloorService;
import com.seatsurfer.service.ReportService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicSeatController {

    private final FloorService floorService;
    private final BookingService bookingService;
    private final ReportService reportService;

    public PublicSeatController(FloorService floorService, BookingService bookingService, ReportService reportService) {
        this.floorService = floorService;
        this.bookingService = bookingService;
        this.reportService = reportService;
    }

    @GetMapping("/floors")
    public ResponseEntity<java.util.List<FloorDtos.FloorSummary>> listFloors(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(floorService.listPublicSummaries(date == null ? LocalDate.now() : date));
    }

    @GetMapping("/floors/{floorId}/availability")
    public ResponseEntity<FloorDtos.FloorLayoutResponse> getAvailability(
        @PathVariable Long floorId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(required = false) String viewerName
    ) {
        return ResponseEntity.ok(floorService.getLayout(floorId, date, viewerName));
    }

    @PostMapping("/bookings")
    public ResponseEntity<BookingDtos.BookingResponse> createBooking(@Valid @RequestBody BookingDtos.CreateBookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @PostMapping("/bookings/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(
        @PathVariable Long bookingId,
        @Valid @RequestBody BookingDtos.CancelBookingRequest request
    ) {
        bookingService.cancelBooking(bookingId, request.requesterName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reports/daily")
    public ResponseEntity<ReportDtos.DailyReportResponse> dailyReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(reportService.getDailyReport(date));
    }
}
