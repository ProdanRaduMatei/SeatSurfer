package com.seatsurfer.service;

import java.time.YearMonth;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonthlyReportScheduler {

    private final EmailReportService emailReportService;

    public MonthlyReportScheduler(EmailReportService emailReportService) {
        this.emailReportService = emailReportService;
    }

    @Scheduled(cron = "${app.reporting.monthly-cron}")
    public void sendPreviousMonthReport() {
        YearMonth previousMonth = YearMonth.now().minusMonths(1);
        emailReportService.sendMonthlyReport(previousMonth.getYear(), previousMonth.getMonthValue());
    }
}
