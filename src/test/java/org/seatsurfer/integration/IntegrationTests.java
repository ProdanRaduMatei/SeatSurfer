package org.seatsurfer.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seatsurfer.domain.Admin;
import org.seatsurfer.domain.Building;
import org.seatsurfer.persistence.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class IntegrationTests {
    @Autowired
    private AdminRepository adminRepository;

    @BeforeEach
    public void setUp() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setName("Admin Name");
        admin.setEmail("admin@gmail.com");
        admin.setPassword("password");

        adminRepository.save(admin);

        Building building = new Building();
        building.setId(1L);
        building.setName("Building Name");
        building.setAdmin(admin);

        admin.setBuildings(List.of(building));
    }

    @AfterEach
    public void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    public void testAddAdminRepository() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setName("Admin Name");
        admin.setEmail("admin@example.com");
        admin.setPassword("password");

        adminRepository.save(admin);
        assertEquals(admin, adminRepository.findById(1L).get());
    }
}
// Todo: pune in setup date necesare, building, floor, seaturi; accent pe relationship; delete all la repositoryuri, in afterEach;