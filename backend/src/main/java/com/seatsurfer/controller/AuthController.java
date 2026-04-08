package com.seatsurfer.controller;

import com.seatsurfer.dto.AuthDtos;
import com.seatsurfer.security.SeatSurferAdminPrincipal;
import com.seatsurfer.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.LoginResponse> login(@Valid @RequestBody AuthDtos.LoginRequest request) {
        return ResponseEntity.ok(authService.startLogin(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthDtos.TokenResponse> verifyOtp(@Valid @RequestBody AuthDtos.VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthDtos.AdminProfile> me(@AuthenticationPrincipal SeatSurferAdminPrincipal principal) {
        return ResponseEntity.ok(authService.getProfile(principal.getUsername()));
    }
}
