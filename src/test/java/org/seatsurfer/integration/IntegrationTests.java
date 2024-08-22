package org.seatsurfer.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seatsurfer.domain.*;
import org.seatsurfer.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class IntegrationTests {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private StoreyRepository storeyRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    public void setUp() {
        Admin admin = new Admin();
        admin.setName("Admin Name");
        admin.setEmail("admin@gmail.com");
        admin.setPassword("password");



        Building building = new Building();
        building.setName("Building Name");
        building.setAdmin(admin);



        Storey storey = new Storey();
        storey.setName("Storey Name");
        storey.setBuilding(building);



        Seat seat1 = new Seat();
        seat1.setLine(1);
        seat1.setCol(1);
        seat1.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat1.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat1.setSeatType("Standing");
        seat1.setStorey(storey);

        Seat seat2 = new Seat();
        seat2.setLine(1);
        seat2.setCol(2);
        seat2.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat2.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat2.setSeatType("2 Monitors");
        seat2.setStorey(storey);

        Seat seat3 = new Seat();
        seat3.setLine(2);
        seat3.setCol(1);
        seat3.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat3.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat3.setSeatType("1 Monitor");
        seat3.setStorey(storey);

        Seat seat4 = new Seat();
        seat4.setLine(2);
        seat4.setCol(2);
        seat4.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat4.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat4.setSeatType("No Monitors");
        seat4.setStorey(storey);



        Booking booking1 = new Booking();
        booking1.setDate(Instant.parse("2021-01-01T00:00:00Z"));
        booking1.setSeat(seat1);
        booking1.setUserName("User Name1");
        booking1.setEmail("user1@gmail.com");

        Booking booking2 = new Booking();
        booking2.setDate(Instant.parse("2021-01-01T00:00:00Z"));
        booking2.setSeat(seat2);
        booking2.setUserName("User Name2");
        booking2.setEmail("user2@gmail.com");

        Booking booking3 = new Booking();
        booking3.setDate(Instant.parse("2021-01-01T00:00:00Z"));
        booking3.setSeat(seat3);
        booking3.setUserName("User Name3");
        booking3.setEmail("user3@gmail.com");

        Booking booking4 = new Booking();
        booking4.setDate(Instant.parse("2021-01-01T00:00:00Z"));
        booking4.setSeat(seat1);
        booking4.setUserName("User Name4");
        booking4.setEmail("user4@gmail.com");


        seat1.setBookings(new ArrayList<>(List.of(booking1, booking4)));
        seat2.setBookings(new ArrayList<>(List.of(booking2)));
        seat3.setBookings(new ArrayList<>(List.of(booking3)));
        storey.setSeats(new ArrayList<>(List.of(seat1, seat2, seat3, seat4)));
        building.setStoreys(new ArrayList<>(List.of(storey)));
        admin.setBuildings(new ArrayList<>(List.of(building)));

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        seatRepository.save(seat1);
        seatRepository.save(seat2);
        seatRepository.save(seat3);
        seatRepository.save(seat4);
        storeyRepository.save(storey);
        buildingRepository.save(building);
        adminRepository.save(admin);
    }

    @AfterEach
    public void tearDown() {
        adminRepository.deleteAll();
        buildingRepository.deleteAll();
        storeyRepository.deleteAll();
        seatRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void testAdmin() {
        Admin admin = adminRepository.findAll().get(0);
        assertEquals("Admin Name", admin.getName());
        assertEquals("admin@gmail.com", admin.getEmail());
        assertEquals("password", admin.getPassword());
        assertEquals(1, adminRepository.findAll().size());
        assertEquals(1, admin.getBuildings().size());
    }

    @Test
    public void testBuilding() {
        Building building = buildingRepository.findAll().get(0);
        assertEquals("Building Name", building.getName());
        assertEquals(1, buildingRepository.findAll().size());
        assertEquals(1, building.getStoreys().size());
    }

    @Test
    public void testStorey() {
        Storey storey = storeyRepository.findAll().get(0);
        assertEquals("Storey Name", storey.getName());
        assertEquals(1, storeyRepository.findAll().size());
        assertEquals(4, storey.getSeats().size());
    }

    @Test
    public void testSeat() {
        Seat seat = seatRepository.findAll().get(3);
        assertEquals(2, seat.getLine());
        assertEquals(2, seat.getCol());
        assertEquals(Instant.parse("2021-01-01T00:00:00Z"), seat.getCreationDate());
        assertEquals(Instant.parse("2021-01-01T00:00:00Z"), seat.getEndAvailabilityDate());
        assertEquals("No Monitors", seat.getSeatType());
        assertEquals(4, seatRepository.findAll().size());
        assertEquals(0, seat.getBookings().size());
    }

    @Test
    public void testBooking() {
        Booking booking = bookingRepository.findAll().get(0);
        assertEquals(Instant.parse("2021-01-01T00:00:00Z"), booking.getDate());
        assertEquals("User Name1", booking.getUserName());
        assertEquals("user1@gmail.com", booking.getEmail());
        assertEquals(4, bookingRepository.findAll().size());
    }
}