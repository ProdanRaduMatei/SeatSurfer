package com.seatsurfer.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Auth auth = new Auth();
    private final Cors cors = new Cors();
    private final Reporting reporting = new Reporting();

    public Auth getAuth() {
        return auth;
    }

    public Cors getCors() {
        return cors;
    }

    public Reporting getReporting() {
        return reporting;
    }

    public static class Auth {
        private String jwtSecret = "change-me-before-production-change-me-before-production";
        private long jwtExpirationMinutes = 480;
        private long otpExpirationMinutes = 10;
        private boolean exposeDevOtp = true;

        public String getJwtSecret() {
            return jwtSecret;
        }

        public void setJwtSecret(String jwtSecret) {
            this.jwtSecret = jwtSecret;
        }

        public long getJwtExpirationMinutes() {
            return jwtExpirationMinutes;
        }

        public void setJwtExpirationMinutes(long jwtExpirationMinutes) {
            this.jwtExpirationMinutes = jwtExpirationMinutes;
        }

        public long getOtpExpirationMinutes() {
            return otpExpirationMinutes;
        }

        public void setOtpExpirationMinutes(long otpExpirationMinutes) {
            this.otpExpirationMinutes = otpExpirationMinutes;
        }

        public boolean isExposeDevOtp() {
            return exposeDevOtp;
        }

        public void setExposeDevOtp(boolean exposeDevOtp) {
            this.exposeDevOtp = exposeDevOtp;
        }
    }

    public static class Cors {
        private List<String> allowedOrigins = new ArrayList<>(List.of(
            "http://localhost:4173",
            "http://127.0.0.1:4173",
            "http://localhost:5500",
            "http://127.0.0.1:5500",
            "http://localhost:8081",
            "http://127.0.0.1:8081"
        ));

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
    }

    public static class Reporting {
        private boolean emailEnabled = false;
        private String monthlyCron = "0 0 8 1 * *";
        private List<String> recipients = new ArrayList<>();

        public boolean isEmailEnabled() {
            return emailEnabled;
        }

        public void setEmailEnabled(boolean emailEnabled) {
            this.emailEnabled = emailEnabled;
        }

        public String getMonthlyCron() {
            return monthlyCron;
        }

        public void setMonthlyCron(String monthlyCron) {
            this.monthlyCron = monthlyCron;
        }

        public List<String> getRecipients() {
            return recipients;
        }

        public void setRecipients(List<String> recipients) {
            this.recipients = recipients;
        }
    }
}
