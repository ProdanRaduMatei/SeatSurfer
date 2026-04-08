package com.seatsurfer.service;

import com.seatsurfer.config.AppProperties;
import com.seatsurfer.dto.ReportDtos;
import jakarta.mail.internet.MimeMessage;
import java.time.YearMonth;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailReportService {

    private final JavaMailSender javaMailSender;
    private final AppProperties appProperties;
    private final ReportService reportService;
    private final PdfReportService pdfReportService;

    public EmailReportService(
        JavaMailSender javaMailSender,
        AppProperties appProperties,
        ReportService reportService,
        PdfReportService pdfReportService
    ) {
        this.javaMailSender = javaMailSender;
        this.appProperties = appProperties;
        this.reportService = reportService;
        this.pdfReportService = pdfReportService;
    }

    public void sendMonthlyReport(int year, int month) {
        if (!appProperties.getReporting().isEmailEnabled()) {
            return;
        }

        List<String> recipients = appProperties.getReporting().getRecipients().stream()
            .filter(value -> value != null && !value.isBlank())
            .flatMap(value -> List.of(value.split(",")).stream())
            .map(String::trim)
            .filter(value -> !value.isBlank())
            .distinct()
            .toList();

        if (recipients.isEmpty()) {
            return;
        }

        ReportDtos.MonthlyReportResponse report = reportService.getMonthlyReport(year, month);
        byte[] pdfBytes = pdfReportService.exportMonthlyReport(report);
        YearMonth yearMonth = YearMonth.of(year, month);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(recipients.toArray(String[]::new));
            helper.setSubject("SeatSurfer monthly booking report - " + yearMonth);
            helper.setText("""
                SeatSurfer monthly report is attached.

                Completed bookings: %d
                Cancellations: %d
                """.formatted(report.totalCompletedBookings(), report.totalCancellations()));
            helper.addAttachment("SeatSurfer-%s.pdf".formatted(yearMonth), new ByteArrayResource(pdfBytes));
            javaMailSender.send(mimeMessage);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to send monthly report email", exception);
        }
    }
}
