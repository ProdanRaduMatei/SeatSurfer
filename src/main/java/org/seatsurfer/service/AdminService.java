package org.seatsurfer.service;

import org.seatsurfer.domain.Admin;
import org.seatsurfer.persistence.AdminRepository;
import org.seatsurfer.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Admin createAdmin(Admin admin) {
        // optional: criptează parola înainte de salvare
        // admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    public Admin updateAdmin(Long id, Admin adminDetails) {
        Admin admin = adminRepository.findById(id).orElseThrow();
        admin.setName(adminDetails.getName());
        admin.setEmail(adminDetails.getEmail());
        admin.setPassword(adminDetails.getPassword());
        admin.setRole(adminDetails.getRole());
        return adminRepository.save(admin);
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    // *** Metoda cheie pentru Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with username: " + username));
        return new CustomUserDetails(admin);
    }

    public Admin getAdminByUsername(String username) {
        return adminRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found: " + username));
    }
}