package com.seatsurfer.security;

import com.seatsurfer.repository.AdminUserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SeatSurferUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    public SeatSurferUserDetailsService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public SeatSurferAdminPrincipal loadUserByUsername(String username) {
        return adminUserRepository.findByUsernameIgnoreCase(username)
            .map(SeatSurferAdminPrincipal::new)
            .orElseThrow(() -> new UsernameNotFoundException("Admin user not found"));
    }
}
