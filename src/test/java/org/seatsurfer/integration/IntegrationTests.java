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
import static org.junit.jupiter.api.Assertions.assertNull;

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
    public void testAddBuilding() {
        Admin admin = adminRepository.findAll().get(0);
        Building building = new Building();
        building.setName("Building Name2");
        admin.addBuilding(building);
        assertEquals(2, admin.getBuildings().size());
        assertEquals(admin, building.getAdmin());
    }

    @Test
    public void testRemoveBuilding() {
        Admin admin = adminRepository.findAll().get(0);
        Building building = buildingRepository.findAll().get(0);
        admin.removeBuilding(building);
        assertEquals(0, admin.getBuildings().size());
        assertNull(building.getAdmin());
    }

    @Test
    public void testAddStorey() {
        Building building = buildingRepository.findAll().get(0);
        Storey storey = new Storey();
        storey.setName("Storey Name2");
        building.addStorey(storey);
        assertEquals(2, building.getStoreys().size());
        assertEquals(building, storey.getBuilding());
    }

    @Test
    public void testRemoveStorey() {
        Building building = buildingRepository.findAll().get(0);
        Storey storey = storeyRepository.findAll().get(0);
        building.removeStorey(storey);
        assertEquals(0, building.getStoreys().size());
        assertNull(storey.getBuilding());
    }

    @Test
    public void testAddSeat() {
        Storey storey = storeyRepository.findAll().get(0);
        Seat seat = new Seat();
        seat.setLine(3);
        seat.setCol(3);
        seat.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat.setSeatType("No Monitors");
        storey.addSeat(seat);
        assertEquals(5, storey.getSeats().size());
        assertEquals(storey, seat.getStorey());
    }

    @Test
    public void testRemoveSeat() {
        Storey storey = storeyRepository.findAll().get(0);
        Seat seat = seatRepository.findAll().get(0);
        storey.removeSeat(seat);
        assertEquals(3, storey.getSeats().size());
        assertNull(seat.getStorey());
    }

    @Test
    public void testAddBooking() {
        Seat seat = seatRepository.findAll().get(0);
        Booking booking = new Booking();
        booking.setDate(Instant.parse("2021-01-01T00:00:00Z"));
        booking.setSeat(seat);
        booking.setUserName("User Name5");
        booking.setEmail("user5@gmail.com");
        seat.addBooking(booking);
        assertEquals(3, seat.getBookings().size());
        assertEquals(seat, booking.getSeat());
    }

    @Test
    public void testRemoveBooking() {
        Seat seat = seatRepository.findAll().get(0);
        Booking booking = bookingRepository.findAll().get(0);
        seat.removeBooking(booking);
        assertEquals(1, seat.getBookings().size());
        assertNull(booking.getSeat());
    }
}