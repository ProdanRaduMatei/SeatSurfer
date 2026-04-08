package com.seatsurfer.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record LoginRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password
    ) {
    }

    public record LoginResponse(
        String challengeId,
        LocalDateTime expiresAt,
        String delivery,
        String devOtpCode,
        AdminProfile admin
    ) {
    }

    public record VerifyOtpRequest(
        @NotBlank(message = "Challenge id is required")
        String challengeId,
        @NotBlank(message = "OTP code is required")
        String code
    ) {
    }

    public record TokenResponse(
        String token,
        LocalDateTime expiresAt,
        AdminProfile admin
    ) {
    }

    public record AdminProfile(
        Long id,
        String username,
        String fullName,
        String email,
        LocalDateTime lastLoginAt
    ) {
    }
}
