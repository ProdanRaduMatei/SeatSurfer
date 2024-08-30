package org.seatsurfer.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seatsurfer.domain.*;
import org.seatsurfer.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class IntegrationTests {
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
        //admin
        Admin admin = new Admin();
        admin.setName("Admin Name");
        admin.setEmail("admin@gmail.com");
        admin.setPassword("password");

        //buildings
        Building building1 = new Building();
        building1.setName("Building Name1");
        building1.setAdmin(admin);

        Building building2 = new Building();
        building2.setName("Building Name2");
        building2.setAdmin(admin);

        //storeys
        Storey storey1 = new Storey();
        storey1.setName("Storey Name1");
        storey1.setBuilding(building1);

        Storey storey2 = new Storey();
        storey2.setName("Storey Name2");
        storey2.setBuilding(building1);

        //seats
        Seat seat1 = new Seat();
        seat1.setLine(1);
        seat1.setCol(1);
        seat1.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat1.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat1.setSeatType("Standing");
        seat1.setStorey(storey1);

        Seat seat2 = new Seat();
        seat2.setLine(1);
        seat2.setCol(2);
        seat2.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat2.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat2.setSeatType("2 Monitors");
        seat2.setStorey(storey1);

        Seat seat3 = new Seat();
        seat3.setLine(2);
        seat3.setCol(1);
        seat3.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat3.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat3.setSeatType("1 Monitor");
        seat3.setStorey(storey1);

        Seat seat4 = new Seat();
        seat4.setLine(2);
        seat4.setCol(2);
        seat4.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat4.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat4.setSeatType("No Monitors");
        seat4.setStorey(storey1);

        Seat seat5 = new Seat();
        seat5.setLine(1);
        seat5.setCol(1);
        seat5.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat5.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat5.setSeatType("Standing");
        seat5.setStorey(storey2);

        Seat seat6 = new Seat();
        seat6.setLine(1);
        seat6.setCol(2);
        seat6.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat6.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat6.setSeatType("2 Monitors");
        seat6.setStorey(storey2);

        Seat seat7 = new Seat();
        seat7.setLine(2);
        seat7.setCol(1);
        seat7.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat7.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat7.setSeatType("1 Monitor");
        seat7.setStorey(storey2);

        Seat seat8 = new Seat();
        seat8.setLine(2);
        seat8.setCol(2);
        seat8.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat8.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat8.setSeatType("No Monitors");
        seat8.setStorey(storey2);

        //bookings
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
        storey1.setSeats(new ArrayList<>(List.of(seat1, seat2, seat3, seat4)));
        storey2.setSeats(new ArrayList<>(List.of(seat5, seat6, seat7, seat8)));
        building1.setStoreys(new ArrayList<>(List.of(storey1, storey2)));
        admin.setBuildings(new ArrayList<>(List.of(building1, building2)));

        bookingRepository.saveAll(List.of(booking1, booking2, booking3, booking4));
        seatRepository.saveAll(List.of(seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8));
        storeyRepository.saveAll(List.of(storey1, storey2));
        buildingRepository.saveAll(List.of(building1, building2));
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
    void testAddBuilding() {
        Admin admin = adminRepository.findAll().get(0);
        Building building = new Building();
        building.setName("Building Name2");
        admin.addBuilding(building);
        assertEquals(3, admin.getBuildings().size());
        assertEquals(admin, building.getAdmin());
    }

    @Test
    void testUpdateBuilding() {
        Admin admin = adminRepository.findAll().get(0);
        final Building building = admin.getBuildings().get(0);
        building.setName("Building Name2");
        building.setAdmin(building.getAdmin());

        final Admin updatedAdmin = adminRepository.saveAndFlush(admin);
        assertEquals("Building Name2", updatedAdmin.getBuildings().get(0).getName());
    }

    @Test
    void testRemoveBuilding() {
        Admin admin = adminRepository.findAll().get(0);
        Building building = buildingRepository.findAll().get(0);
        final int buildingInitialSize = buildingRepository.findAll().size();
        final int storeyInitialSize = storeyRepository.findAll().size();
        final int seatInitialSize = seatRepository.findAll().size();
        admin.removeBuilding(building);

        adminRepository.saveAndFlush(admin);
        assertEquals(buildingInitialSize - 1, buildingRepository.findAll().size());
        assertEquals(storeyInitialSize - 2, storeyRepository.findAll().size());
        assertEquals(seatInitialSize - 8, seatRepository.findAll().size());
    }

    @Test
    void testAddStorey() {
        Building building = buildingRepository.findAll().get(0);
        Storey storey = new Storey();
        storey.setName("Storey Name2");
        building.addStorey(storey);
        assertEquals(3, building.getStoreys().size());
        assertEquals(building, storey.getBuilding());
    }

    @Test
    void testUpdateStorey() {
        Building building = buildingRepository.findAll().get(0);
        final Storey storey = building.getStoreys().get(0);
        storey.setName("Storey Name2");
        storey.setBuilding(storey.getBuilding());

        final Building updatedBuilding = buildingRepository.saveAndFlush(building);
        assertEquals("Storey Name2", updatedBuilding.getStoreys().get(0).getName());
    }

    @Test
    void testRemoveStorey() {
        Building building = buildingRepository.findAll().get(0);
        Storey storey = storeyRepository.findAll().get(0);
        final int storeyInitialSize = storeyRepository.findAll().size();
        final int seatInitialSize = seatRepository.findAll().size();
        building.removeStorey(storey);

        buildingRepository.saveAndFlush(building);
        assertEquals(storeyInitialSize - 1, storeyRepository.findAll().size());
        assertEquals(seatInitialSize - 4, seatRepository.findAll().size());
    }

    @Test
    void testAddSeat() {
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
    void testUpdateSeat() {
        Storey storey = storeyRepository.findAll().get(0);
        final Seat seat = storey.getSeats().get(0);
        seat.setLine(3);
        seat.setCol(3);
        seat.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
        seat.setSeatType("No Monitors");
        seat.setStorey(seat.getStorey());

        final Storey updatedStorey = storeyRepository.saveAndFlush(storey);
        assertEquals(3, updatedStorey.getSeats().get(0).getLine());
        assertEquals(3, updatedStorey.getSeats().get(0).getCol());
        assertEquals(Instant.parse("2021-01-01T00:00:00Z"), updatedStorey.getSeats().get(0).getCreationDate());
        assertEquals(Instant.parse("2021-01-01T00:00:00Z"), updatedStorey.getSeats().get(0).getEndAvailabilityDate());
        assertEquals("No Monitors", updatedStorey.getSeats().get(0).getSeatType());
    }

    @Test
    void testRemoveSeat() {
        Storey storey = storeyRepository.findAll().get(0);
        Seat seat = seatRepository.findAll().get(0);
        final int seatInitialSize = seatRepository.findAll().size();
        final int bookingInitialSize = bookingRepository.findAll().size();
        storey.removeSeat(seat);

        storeyRepository.saveAndFlush(storey);
        assertEquals(seatInitialSize - 1, seatRepository.findAll().size());
        assertEquals(bookingInitialSize - 2, bookingRepository.findAll().size());
    }

    @Test
    void testAddBooking() {
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
    void testUpdateBooking() {
        Seat seat = seatRepository.findAll().get(0);
        final Booking booking = seat.getBookings().get(0);
        booking.setDate(Instant.parse("2021-01-01T00:00:00Z"));
        booking.setUserName("User Name5");
        booking.setEmail("user5@gmail.com");
        booking.setSeat(booking.getSeat());

        final Seat updatedSeat = seatRepository.saveAndFlush(seat);
        assertEquals("user5@gmail.com", updatedSeat.getBookings().get(0).getEmail());
        assertEquals("User Name5", updatedSeat.getBookings().get(0).getUserName());
        assertEquals(Instant.parse("2021-01-01T00:00:00Z"), updatedSeat.getBookings().get(0).getDate());
    }

    @Test
    void testRemoveBooking() {
        Seat seat = seatRepository.findAll().get(0);
        Booking booking = bookingRepository.findAll().get(0);
        final int bookingInitialSize = bookingRepository.findAll().size();
        seat.removeBooking(booking);

        seatRepository.saveAndFlush(seat);
        assertEquals(bookingInitialSize - 1, bookingRepository.findAll().size());
    }
}