package com.seatsurfer.controller;

import com.seatsurfer.dto.ReportDtos;
import com.seatsurfer.service.EmailReportService;
import com.seatsurfer.service.PdfReportService;
import com.seatsurfer.service.ReportService;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

    private final ReportService reportService;
    private final PdfReportService pdfReportService;
    private final EmailReportService emailReportService;

    public AdminReportController(
        ReportService reportService,
        PdfReportService pdfReportService,
        EmailReportService emailReportService
    ) {
        this.reportService = reportService;
        this.pdfReportService = pdfReportService;
        this.emailReportService = emailReportService;
    }

    @GetMapping("/daily")
    public ResponseEntity<ReportDtos.DailyReportResponse> daily(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(reportService.getDailyReport(date));
    }

    @GetMapping("/load")
    public ResponseEntity<ReportDtos.LoadReportResponse> load(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(reportService.getLoadReport(from, to));
    }

    @GetMapping("/monthly")
    public ResponseEntity<ReportDtos.MonthlyReportResponse> monthly(
        @RequestParam int year,
        @RequestParam int month
    ) {
        return ResponseEntity.ok(reportService.getMonthlyReport(year, month));
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> export(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        LocalDate rangeFrom = from == null ? date : from;
        LocalDate rangeTo = to == null ? date.plusDays(30) : to;
        byte[] pdf = pdfReportService.exportManagementReport(
            date,
            reportService.getDailyReport(date),
            reportService.getLoadReport(rangeFrom, rangeTo)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment()
            .filename("SeatSurfer-report-" + date + ".pdf")
            .build());
        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }

    @PostMapping("/monthly/email")
    public ResponseEntity<Void> emailMonthly(
        @RequestParam int year,
        @RequestParam int month
    ) {
        emailReportService.sendMonthlyReport(year, month);
        return ResponseEntity.accepted().build();
    }
}
