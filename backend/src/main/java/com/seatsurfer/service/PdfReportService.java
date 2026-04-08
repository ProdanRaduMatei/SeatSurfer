package com.seatsurfer.service;

import com.seatsurfer.dto.FloorDtos;
import com.seatsurfer.dto.ReportDtos;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

@Service
public class PdfReportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM uuuu");

    public byte[] exportManagementReport(LocalDate date, ReportDtos.DailyReportResponse dailyReport, ReportDtos.LoadReportResponse loadReport) {
        List<String> lines = new ArrayList<>();
        lines.add("SeatSurfer Management Report");
        lines.add("Daily summary for " + DATE_FORMATTER.format(date));
        lines.add("");
        lines.add("Floor                     Total   Booked   Free   Load%");
        lines.add("--------------------------------------------------------");
        for (FloorDtos.FloorSummary floor : dailyReport.floors()) {
            lines.add(String.format("%-24s %5d %8d %6d %7.2f",
                floor.name(), floor.totalSeats(), floor.bookedSeats(), floor.availableSeats(), floor.loadPercentage()));
        }
        lines.add("");
        lines.add("Totals: seats=%d booked=%d free=%d load=%.2f%%".formatted(
            dailyReport.totals().totalSeats(),
            dailyReport.totals().bookedSeats(),
            dailyReport.totals().availableSeats(),
            dailyReport.totals().loadPercentage()
        ));
        lines.add("");
        lines.add("Load overview from %s to %s".formatted(
            DATE_FORMATTER.format(loadReport.from()),
            DATE_FORMATTER.format(loadReport.to())
        ));
        lines.add("--------------------------------------------------------");
        for (ReportDtos.FloorLoadReport floor : loadReport.floors()) {
            lines.add(String.format("%s | avg %.2f%% | peak %.2f%% | bookings %d",
                floor.floorName(),
                floor.averageLoadPercentage(),
                floor.peakLoadPercentage(),
                floor.totalBookings()));
        }
        return renderPdf(lines);
    }

    public byte[] exportMonthlyReport(ReportDtos.MonthlyReportResponse report) {
        List<String> lines = new ArrayList<>();
        lines.add("SeatSurfer Monthly Report");
        lines.add("Period: %d-%02d".formatted(report.year(), report.month()));
        lines.add("");
        lines.add("Completed bookings: " + report.totalCompletedBookings());
        lines.add("Cancellations: " + report.totalCancellations());
        lines.add("");
        for (ReportDtos.FloorLoadReport floor : report.floors()) {
            lines.add(String.format("%s | avg %.2f%% | peak %.2f%% | booking volume %d",
                floor.floorName(),
                floor.averageLoadPercentage(),
                floor.peakLoadPercentage(),
                floor.totalBookings()));
        }
        return renderPdf(lines);
    }

    private byte[] renderPdf(List<String> lines) {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDType1Font font = PDType1Font.HELVETICA;
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(font, 11);
            contentStream.setLeading(15f);
            contentStream.newLineAtOffset(48, 780);

            float currentY = 780f;
            for (String line : lines) {
                if (currentY < 70f) {
                    contentStream.endText();
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(font, 11);
                    contentStream.setLeading(15f);
                    contentStream.newLineAtOffset(48, 780);
                    currentY = 780f;
                }
                contentStream.showText(line);
                contentStream.newLine();
                currentY -= 15f;
            }

            contentStream.endText();
            contentStream.close();
            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to generate PDF report", exception);
        }
    }
}
