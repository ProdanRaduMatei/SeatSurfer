package com.seatsurfer.service;

import com.seatsurfer.config.AppProperties;
import com.seatsurfer.dto.AuthDtos;
import com.seatsurfer.entity.AdminUser;
import com.seatsurfer.entity.AuthChallenge;
import com.seatsurfer.repository.AdminUserRepository;
import com.seatsurfer.repository.AuthChallengeRepository;
import com.seatsurfer.security.JwtService;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final AdminUserRepository adminUserRepository;
    private final AuthChallengeRepository authChallengeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender javaMailSender;
    private final AppProperties appProperties;
    private final Environment environment;

    public AuthService(
        AdminUserRepository adminUserRepository,
        AuthChallengeRepository authChallengeRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        JavaMailSender javaMailSender,
        AppProperties appProperties,
        Environment environment
    ) {
        this.adminUserRepository = adminUserRepository;
        this.authChallengeRepository = authChallengeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.javaMailSender = javaMailSender;
        this.appProperties = appProperties;
        this.environment = environment;
    }

    @Transactional
    public AuthDtos.LoginResponse startLogin(AuthDtos.LoginRequest request) {
        AdminUser adminUser = adminUserRepository.findByUsernameIgnoreCase(request.username())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!adminUser.isActive() || !passwordEncoder.matches(request.password(), adminUser.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String otpCode = String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
        AuthChallenge challenge = new AuthChallenge();
        challenge.setId(UUID.randomUUID().toString());
        challenge.setAdminUser(adminUser);
        challenge.setCodeHash(passwordEncoder.encode(otpCode));
        challenge.setCreatedAt(LocalDateTime.now());
        challenge.setExpiresAt(LocalDateTime.now().plusMinutes(appProperties.getAuth().getOtpExpirationMinutes()));
        authChallengeRepository.save(challenge);

        String delivery = "dev-response";
        if (mailConfigured()) {
            sendOtpEmail(adminUser, otpCode);
            delivery = "email";
        }

        return new AuthDtos.LoginResponse(
            challenge.getId(),
            challenge.getExpiresAt(),
            delivery,
            appProperties.getAuth().isExposeDevOtp() ? otpCode : null,
            toProfile(adminUser)
        );
    }

    @Transactional
    public AuthDtos.TokenResponse verifyOtp(AuthDtos.VerifyOtpRequest request) {
        AuthChallenge challenge = authChallengeRepository.findById(request.challengeId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication challenge not found"));

        if (challenge.getConsumedAt() != null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication challenge has already been used");
        }
        if (challenge.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication challenge has expired");
        }
        if (!passwordEncoder.matches(request.code(), challenge.getCodeHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid OTP code");
        }

        AdminUser adminUser = challenge.getAdminUser();
        challenge.setConsumedAt(LocalDateTime.now());
        adminUser.setLastLoginAt(LocalDateTime.now());

        String token = jwtService.generateToken(adminUser);
        return new AuthDtos.TokenResponse(token, jwtService.expiresAt(), toProfile(adminUser));
    }

    public AuthDtos.AdminProfile getProfile(String username) {
        AdminUser adminUser = adminUserRepository.findByUsernameIgnoreCase(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin user not found"));
        return toProfile(adminUser);
    }

    private AuthDtos.AdminProfile toProfile(AdminUser adminUser) {
        return new AuthDtos.AdminProfile(
            adminUser.getId(),
            adminUser.getUsername(),
            adminUser.getFullName(),
            adminUser.getEmail(),
            adminUser.getLastLoginAt()
        );
    }

    private boolean mailConfigured() {
        String host = environment.getProperty("spring.mail.host", "");
        return host != null && !host.isBlank();
    }

    private void sendOtpEmail(AdminUser adminUser, String otpCode) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setTo(adminUser.getEmail());
            helper.setSubject("SeatSurfer login verification code");
            helper.setText("""
                Hello %s,

                Your SeatSurfer verification code is: %s

                It expires in %d minutes.
                """.formatted(adminUser.getFullName(), otpCode, appProperties.getAuth().getOtpExpirationMinutes()));
            javaMailSender.send(message);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Unable to send OTP email");
        }
    }
}
