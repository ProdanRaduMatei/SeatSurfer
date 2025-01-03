package org.seatsurfer.web;

import org.seatsurfer.domain.Admin;
import org.seatsurfer.service.AdminService;
import org.seatsurfer.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {
        // admin.getName() = username, admin.getPassword() = password
        try {
            // Forțăm Spring Security să autentifice (va apela loadUserByUsername())
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            admin.getName(),
                            admin.getPassword()
                    )
            );

            // Dacă nu sare excepție, credentials sunt valide
            // Luăm userul complet din DB
            var dbAdmin = adminService.getAdminByUsername(admin.getName());
            // Sau: Admin found = (Admin) adminService.loadUserByUsername(admin.getName());
            // Generează token
            String token = jwtUtil.generateToken(dbAdmin.getName(), dbAdmin.getRole());

            return ResponseEntity.ok(token);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}