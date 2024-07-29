package org.seatsurfer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.seatsurfer.domain.Buildings;
import org.seatsurfer.domain.Seats;
import org.seatsurfer.domain.Storeys;
import org.seatsurfer.domain.Users;
import org.seatsurfer.repositories.BuildingRepository;
import org.seatsurfer.repositories.SeatRepository;
import org.seatsurfer.repositories.UsersRepository;
import org.seatsurfer.repositories.StoreyRepository;
import org.seatsurfer.services.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class SeatSurferApplicationTests {

    @Test
    void contextLoads() {
    }

    // Test the Users class
    @Test
    void testUsers() {
        Users john = new Users("John Doe", true);
        assertEquals("John Doe", john.getName());
        assertEquals(true, john.getIsAdmin());
        john.setName("Jane Doe");
        john.setIsAdmin(false);
        assertEquals("Jane Doe", john.getName());
        assertEquals(false, john.getIsAdmin());
        john = null;
        assertNull(john);
    }

    // Test the Buildings class
    @Test
    void testBuildings() {
        Buildings buildings = new Buildings();
        buildings.setId(1L);
        assertEquals(1L, buildings.getId());
        buildings.setId(2L);
        assertEquals(2L, buildings.getId());
        buildings = null;
        assertNull(buildings);
    }

    // Test the Storeys class
    @Test
    void testStoreys() {
        Storeys storeys = new Storeys(1L);
        assertEquals(1, storeys.getBuildingId());
        storeys.setBuildingId(2L);
        assertEquals(2, storeys.getBuildingId());
        storeys = null;
        assertNull(storeys);
    }

    // Test the Seats class
    @Test
    void testSeats() {
        Seats seats = new Seats();
        seats.setId(1L);
        assertEquals(1L, seats.getId());
        seats.setRow(1);
        assertEquals(1, seats.getRow());
        seats.setCol(1);
        assertEquals(1, seats.getCol());
        seats.setSeat(true);
        assertEquals(true, seats.getSeat());
        seats.setOccupied(true);
        assertEquals(true, seats.getOccupied());
        seats.setOccupiedDate(null);
        assertNull(seats.getOccupiedDate());
        seats.setStoreyId(1L);
        assertEquals(1L, seats.getStoreyId());
        seats.setUserId(1L);
        assertEquals(1L, seats.getUserId());
        seats = null;
        assertNull(seats);
    }

    // Test the User services
    @Mock
    private UsersRepository usersRepository;

    @Mock
    private SeatRepository seatsRepository;

    @Mock
    private StoreyRepository storeysRepository;

    @Mock
    private BuildingRepository buildingsRepository;

    @InjectMocks
    private UsersServices usersServices;

    @InjectMocks
    private SeatsServices seatsServices;

    @InjectMocks
    private StoreysServices storeysServices;

    @InjectMocks
    private BuildingsServices buildingsServices;

    @InjectMocks
    private Services services;

    @InjectMocks
    private SeatSurferApplicationTests seatSurferApplicationTests;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        Users user = new Users();
        user.setId(1L);
        user.setName("John Doe");
        user.setIsAdmin(true);

        when(usersRepository.save(any(Users.class))).thenReturn(user);

        Users createdUser = usersServices.createUser(user);

        assertNotNull(createdUser, "The created user should not be null");
        assertEquals(1L, createdUser.getId());
        assertEquals("John Doe", createdUser.getName());
        assertTrue(createdUser.getIsAdmin());
    }

    @Test
    void testGetUser() {
        Users user = new Users();
        user.setId(1L);
        user.setName("John Doe");
        user.setIsAdmin(true);

        when(usersRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        Optional<Users> foundUser = usersServices.getUserById(1L);

        assertEquals("John Doe", foundUser.get().getName());
        assertTrue(foundUser.get().getIsAdmin());
    }

    @Test
    void testGetUsers() {
        Users user = new Users();
        user.setId(1L);
        user.setName("John Doe");
        user.setIsAdmin(true);

        when(usersRepository.findAll()).thenReturn(java.util.List.of(user));

        List<Users> foundUsers = usersServices.getAllUsers();

        assertNotNull(foundUsers);
        assertEquals(1, foundUsers.size());
        assertEquals("John Doe", foundUsers.get(0).getName());
        assertTrue(foundUsers.get(0).getIsAdmin());
    }

    @Test
    void testUpdateUser() {
        Users oldUser = new Users();
        oldUser.setId(1L);
        oldUser.setName("John Doe");
        oldUser.setIsAdmin(true);

        Users updatedUser = new Users();
        updatedUser.setId(1L);
        updatedUser.setName("Jane Doe");
        updatedUser.setIsAdmin(false);

        when(usersRepository.findById(1L)).thenReturn(java.util.Optional.of(oldUser));
        when(usersRepository.save(oldUser)).thenReturn(oldUser);

        usersServices.updateUser(1L, updatedUser);

        assertNotNull(oldUser);
        assertEquals("Jane Doe", updatedUser.getName());
        assertFalse(updatedUser.getIsAdmin());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(usersRepository).deleteById(1L);

        usersServices.deleteUser(1L);

        verify(usersRepository, times(1)).deleteById(1L);
    }

    // Test the Buildings services
    @Test
    void testCreateBuilding() {
        Buildings building = new Buildings();
        building.setId(1L);

        when(buildingsRepository.save(any(Buildings.class))).thenReturn(building);

        Buildings createdBuilding = buildingsServices.createBuilding(building);

        assertNotNull(createdBuilding);
        assertEquals(1L, createdBuilding.getId());
    }

    @Test
    void testGetBuilding() {
        Buildings building = new Buildings();
        building.setId(1L);

        when(buildingsRepository.findById(1L)).thenReturn(java.util.Optional.of(building));

        Optional<Buildings> foundBuilding = buildingsServices.getBuildingById(1L);

        assertNotNull(foundBuilding.isPresent());
        assertEquals(1L, foundBuilding.get().getId());
    }

    @Test
    void testGetBuildings() {
        Buildings building = new Buildings();
        building.setId(1L);

        when(buildingsRepository.findAll()).thenReturn(java.util.List.of(building));

        List<Buildings> foundBuildings = buildingsServices.getAllBuildings();

        assertNotNull(foundBuildings);
        assertEquals(1, foundBuildings.size());
        assertEquals(1L, foundBuildings.get(0).getId());
    }

    @Test
    void testDeleteBuilding() {
        doNothing().when(buildingsRepository).deleteById(1L);

        buildingsServices.deleteBuilding(1L);

        verify(buildingsRepository, times(1)).deleteById(1L);
    }

    // Test the Storeys services
    @Test
    void testCreateStorey() {
        Storeys storey = new Storeys();
        storey.setId(1L);
        storey.setBuildingId(1L);

        when(storeysRepository.save(storey)).thenReturn(storey);

        Storeys createdStorey = storeysServices.createStorey(storey);

        assertNotNull(createdStorey);
        assertEquals(1L, createdStorey.getId());
        assertEquals(1L, createdStorey.getBuildingId());
    }

    @Test
    void testGetStorey() {
        Storeys storey = new Storeys();
        storey.setId(1L);
        storey.setBuildingId(1L);

        when(storeysRepository.findById(1L)).thenReturn(java.util.Optional.of(storey));

        Optional<Storeys> foundStorey = storeysServices.getStoreyById(1L);

        assertNotNull(foundStorey.isPresent());
        assertEquals(1L, foundStorey.get().getId());
        assertEquals(1L, foundStorey.get().getBuildingId());
    }

    @Test
    void testGetStoreys() {
        Storeys storey = new Storeys();
        storey.setId(1L);
        storey.setBuildingId(1L);

        when(storeysRepository.findAll()).thenReturn(java.util.List.of(storey));

        List<Storeys> foundStoreys = storeysServices.getAllStoreys();

        assertNotNull(foundStoreys);
        assertEquals(1, foundStoreys.size());
        assertEquals(1L, foundStoreys.get(0).getId());
        assertEquals(1L, foundStoreys.get(0).getBuildingId());
    }

    @Test
    void testUpdateStorey() {
        Storeys oldStorey = new Storeys();
        oldStorey.setId(1L);
        oldStorey.setBuildingId(1L);

        Storeys updatedStorey = new Storeys();
        updatedStorey.setId(1L);
        updatedStorey.setBuildingId(2L);

        when(storeysRepository.findById(1L)).thenReturn(java.util.Optional.of(oldStorey));
        when(storeysRepository.save(oldStorey)).thenReturn(oldStorey);

        storeysServices.updateStorey(1L, updatedStorey);

        assertNotNull(oldStorey);
        assertEquals(1L, updatedStorey.getId());
        assertEquals(2L, updatedStorey.getBuildingId());
    }

    @Test
    void testDeleteStorey() {
        doNothing().when(storeysRepository).deleteById(1L);

        storeysServices.deleteStorey(1L);

        verify(storeysRepository, times(1)).deleteById(1L);
    }

    // Test the Seats services
    @Test
    void testCreateSeat() {
        Seats seat = new Seats();
        seat.setId(1L);
        seat.setRow(1);
        seat.setCol(1);
        seat.setSeat(true);
        seat.setOccupied(true);
        seat.setOccupiedDate(null);
        seat.setStoreyId(1L);
        seat.setUserId(1L);
        seat.setAdminId(1L);

        when(seatsRepository.save(any(Seats.class))).thenReturn(seat);

        Seats createdSeat = seatsServices.createSeat(seat);

        assertNotNull(createdSeat);
        assertEquals(1L, createdSeat.getId());
        assertEquals(1, createdSeat.getRow());
        assertEquals(1, createdSeat.getCol());
        assertEquals(true, createdSeat.getSeat());
        assertEquals(true, createdSeat.getOccupied());
        assertNull(createdSeat.getOccupiedDate());
        assertEquals(1L, createdSeat.getStoreyId());
        assertEquals(1L, createdSeat.getUserId());
        assertEquals(1L, createdSeat.getAdminId());
    }

    @Test
    void testGetSeat() {
        Seats seat = new Seats();
        seat.setId(1L);
        seat.setRow(1);
        seat.setCol(1);
        seat.setSeat(true);
        seat.setOccupied(true);
        seat.setOccupiedDate(null);
        seat.setStoreyId(1L);
        seat.setUserId(1L);
        seat.setAdminId(1L);

        when(seatsRepository.findById(1L)).thenReturn(Optional.of(seat));

        Optional<Seats> foundSeat = seatsServices.getSeatById(1L);

        assertTrue(foundSeat.isPresent());
        assertEquals(1L, foundSeat.get().getId());
        assertEquals(1, foundSeat.get().getRow());
        assertEquals(1, foundSeat.get().getCol());
        assertEquals(true, foundSeat.get().getSeat());
        assertEquals(true, foundSeat.get().getOccupied());
        assertNull(foundSeat.get().getOccupiedDate());
        assertEquals(1L, foundSeat.get().getStoreyId());
        assertEquals(1L, foundSeat.get().getUserId());
        assertEquals(1L, foundSeat.get().getAdminId());
    }

    @Test
    void testGetSeats() {
        Seats seat = new Seats();
        seat.setId(1L);
        seat.setRow(1);
        seat.setCol(1);
        seat.setSeat(true);
        seat.setOccupied(true);
        seat.setOccupiedDate(null);
        seat.setStoreyId(1L);
        seat.setUserId(1L);
        seat.setAdminId(1L);

        when(seatsRepository.findAll()).thenReturn(List.of(seat));

        List<Seats> foundSeats = seatsServices.getAllSeats();

        assertNotNull(foundSeats);
        assertEquals(1, foundSeats.size());
        assertEquals(1L, foundSeats.get(0).getId());
        assertEquals(1, foundSeats.get(0).getRow());
        assertEquals(1, foundSeats.get(0).getCol());
        assertEquals(true, foundSeats.get(0).getSeat());
        assertEquals(true, foundSeats.get(0).getOccupied());
        assertNull(foundSeats.get(0).getOccupiedDate());
        assertEquals(1L, foundSeats.get(0).getStoreyId());
        assertEquals(1L, foundSeats.get(0).getUserId());
        assertEquals(1L, foundSeats.get(0).getAdminId());
    }

    @Test
    void testUpdateSeat() {
        Seats oldSeat = new Seats();
        oldSeat.setId(1L);
        oldSeat.setRow(1);
        oldSeat.setCol(1);
        oldSeat.setSeat(true);
        oldSeat.setOccupied(true);
        oldSeat.setOccupiedDate(null);
        oldSeat.setStoreyId(1L);
        oldSeat.setUserId(1L);
        oldSeat.setAdminId(1L);

        Seats updatedSeat = new Seats();
        updatedSeat.setId(1L);
        updatedSeat.setRow(2);
        updatedSeat.setCol(2);
        updatedSeat.setSeat(false);
        updatedSeat.setOccupied(false);
        updatedSeat.setOccupiedDate(null);
        updatedSeat.setStoreyId(2L);
        updatedSeat.setUserId(2L);
        updatedSeat.setAdminId(1L);

        when(seatsRepository.findById(1L)).thenReturn(Optional.of(oldSeat));
        when(seatsRepository.save(any(Seats.class))).thenReturn(updatedSeat);

        Seats result = seatsServices.updateSeat(1L, updatedSeat);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(2, result.getRow());
        assertEquals(2, result.getCol());
        assertEquals(false, result.getSeat());
        assertEquals(false, result.getOccupied());
        assertNull(result.getOccupiedDate());
        assertEquals(2L, result.getStoreyId());
        assertEquals(2L, result.getUserId());
        assertEquals(1L, result.getAdminId());
    }

    @Test
    void testDeleteSeat() {
        doNothing().when(seatsRepository).deleteById(1L);

        seatsServices.deleteSeat(1L);

        verify(seatsRepository, times(1)).deleteById(1L);
    }

    // Test the Admin services
    @Test
    void testAddStorey() {
        Storeys storey = new Storeys();
        storey.setId(1L);
        storey.setBuildingId(1L);

        Users admin = new Users();
        admin.setId(1L);
        admin.setIsAdmin(true);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(storeysRepository.save(any(Storeys.class))).thenReturn(storey);

        Storeys result = services.addStorey(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getBuildingId());
    }

    @Test
    void testEditStorey() {
        Storeys storey = new Storeys();
        storey.setId(1L);
        storey.setBuildingId(1L);

        Users admin = new Users();
        admin.setId(1L);
        admin.setIsAdmin(true);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(storeysRepository.findById(1L)).thenReturn(Optional.of(storey));

        services.editStorey(1L, 1L, 2L);

        assertNotNull(storey);
        assertEquals(1L, storey.getId());
        assertEquals(2L, storey.getBuildingId());
    }

    @Test
    void testRemoveStorey() {
        Storeys storey = new Storeys();
        storey.setId(1L);
        storey.setBuildingId(1L);

        Users admin = new Users();
        admin.setId(1L);
        admin.setIsAdmin(true);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(storeysRepository.findById(1L)).thenReturn(Optional.of(storey));

        services.removeStorey(1L, 1L);

        verify(storeysRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAddSeat() {
        Users admin = new Users();
        admin.setId(1L);
        admin.setIsAdmin(true);

        Seats seat = new Seats();
        seat.setId(1L);
        seat.setRow(1);
        seat.setCol(1);
        seat.setSeat(true);
        seat.setOccupied(false);
        seat.setOccupiedDate(null);
        seat.setStoreyId(1L);
        seat.setUserId(null);
        seat.setAdminId(1L);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(seatsRepository.save(any(Seats.class))).thenReturn(seat);

        Seats result = services.addSeat(1L, 1L, 1, 1, true, false, null);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1, result.getRow());
        assertEquals(1, result.getCol());
        assertEquals(true, result.getSeat());
        assertEquals(false, result.getOccupied());
        assertNull(result.getOccupiedDate());
        assertEquals(1L, result.getStoreyId());
        assertNull(result.getUserId());
        assertEquals(1L, result.getAdminId());

        verify(usersRepository, times(1)).findById(1L);
        verify(seatsRepository, times(1)).save(any(Seats.class));
    }

    @Test
    void testEditSeat() {
        Users admin = new Users();
        admin.setId(1L);
        admin.setIsAdmin(true);

        Seats seat = new Seats();
        seat.setId(1L);
        seat.setRow(1);
        seat.setCol(1);
        seat.setSeat(true);
        seat.setOccupied(false);
        seat.setOccupiedDate(null);
        seat.setStoreyId(1L);
        seat.setUserId(null);
        seat.setAdminId(1L);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(seatsRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(seatsRepository.save(any(Seats.class))).thenReturn(seat);

        services.editSeat(1L, 1L, 1L, 2, 2, false, false, null, 1L);

        assertNotNull(seat);
        assertEquals(1L, seat.getId());
        assertEquals(2, seat.getRow());
        assertEquals(2, seat.getCol());
        assertEquals(false, seat.getSeat());
        assertEquals(false, seat.getOccupied());
        assertNull(seat.getOccupiedDate());
        assertEquals(1L, seat.getStoreyId());
        assertEquals(null, seat.getUserId());
        assertEquals(1L, seat.getAdminId());

        verify(usersRepository, times(1)).findById(1L);
        verify(seatsRepository, times(1)).findById(1L);
        verify(seatsRepository, times(1)).save(any(Seats.class)); // Ensure the save method is verified
    }

    @Test
    void testRemoveSeat() {
        Users admin = new Users();
        admin.setId(1L);
        admin.setIsAdmin(true);

        Seats seat = new Seats();
        seat.setId(1L);
        seat.setRow(1);
        seat.setCol(1);
        seat.setSeat(true);
        seat.setOccupied(false);
        seat.setOccupiedDate(null);
        seat.setStoreyId(1L);
        seat.setUserId(null);
        seat.setAdminId(1L);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(seatsRepository.findById(1L)).thenReturn(Optional.of(seat));

        services.removeSeat(1L, 1L);

        verify(seatsRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetBookedSeatsInStorey() {
        Users admin = new Users();
        admin.setId(1L);
        admin.setIsAdmin(true);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        try {
            date = dateFormat.parse("25.07.2024");
        } catch (ParseException e) {
            fail("Date parsing failed");
        }

        Seats seat1 = new Seats();
        seat1.setId(1L);
        seat1.setRow(1);
        seat1.setCol(1);
        seat1.setSeat(true);
        seat1.setOccupied(true);
        seat1.setOccupiedDate(date);
        seat1.setStoreyId(1L);
        seat1.setUserId(1L);
        seat1.setAdminId(1L);

        Seats seat2 = new Seats();
        seat2.setId(2L);
        seat2.setRow(2);
        seat2.setCol(2);
        seat2.setSeat(true);
        seat2.setOccupied(true);
        seat2.setOccupiedDate(date);
        seat2.setStoreyId(1L);
        seat2.setUserId(2L);
        seat2.setAdminId(1L);

        List<Seats> allSeats = List.of(seat1, seat2);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(seatsRepository.findAll()).thenReturn(allSeats);

        List<Seats> bookedSeats = services.getBookedSeatsInStorey(1L, 1L);

        assertNotNull(bookedSeats);
        assertEquals(2, bookedSeats.size());
        assertEquals(1L, bookedSeats.get(0).getId());
        assertEquals(2L, bookedSeats.get(1).getId());
        assertEquals(1L, bookedSeats.get(0).getStoreyId());
        assertEquals(1L, bookedSeats.get(1).getStoreyId());
        assertTrue(bookedSeats.get(0).getOccupied());
        assertTrue(bookedSeats.get(1).getOccupied());
        assertEquals(date, bookedSeats.get(0).getOccupiedDate());
        assertEquals(date, bookedSeats.get(1).getOccupiedDate());

        verify(usersRepository, times(1)).findById(1L);
        verify(seatsRepository, times(1)).findAll();
    }

    @Test
    void testGetBookedSeatsInBuilding() {
        Users admin = new Users();
        admin.setId(1L);
        admin.setIsAdmin(true);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        try {
            date = dateFormat.parse("25.07.2024");
        } catch (ParseException e) {
            fail("Date parsing failed");
        }

        Buildings building = new Buildings();
        building.setId(1L);

        Storeys storey = new Storeys();
        storey.setId(1L);
        storey.setBuildingId(1L);

        Seats seat1 = new Seats();
        seat1.setId(1L);
        seat1.setRow(1);
        seat1.setCol(1);
        seat1.setSeat(true);
        seat1.setOccupied(true);
        seat1.setOccupiedDate(date);
        seat1.setStoreyId(1L);
        seat1.setUserId(1L);
        seat1.setAdminId(1L);

        Seats seat2 = new Seats();
        seat2.setId(2L);
        seat2.setRow(2);
        seat2.setCol(2);
        seat2.setSeat(true);
        seat2.setOccupied(true);
        seat2.setOccupiedDate(date);
        seat2.setStoreyId(1L);
        seat2.setUserId(2L);
        seat2.setAdminId(1L);

        List<Seats> allSeats = List.of(seat1, seat2);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(seatsRepository.findAll()).thenReturn(allSeats);
        when(storeysRepository.findById(1L)).thenReturn(Optional.of(storey));

        List<Seats> bookedSeats = services.getBookedSeatsInBuilding(1L, 1L);

        assertNotNull(bookedSeats);
        assertEquals(2, bookedSeats.size());
        assertEquals(1L, bookedSeats.get(0).getId());
        assertEquals(2L, bookedSeats.get(1).getId());
        assertEquals(1L, bookedSeats.get(0).getStoreyId());
        assertEquals(1L, bookedSeats.get(1).getStoreyId());
        assertTrue(bookedSeats.get(0).getOccupied());
        assertTrue(bookedSeats.get(1).getOccupied());
        assertEquals(date, bookedSeats.get(0).getOccupiedDate());
        assertEquals(date, bookedSeats.get(1).getOccupiedDate());

        verify(usersRepository, times(1)).findById(1L);
        verify(seatsRepository, times(1)).findAll();
        verify(storeysRepository, times(1)).findById(1L);
    }

    @Test
    void testBookSeat() {
        // Create mock user
        Users user = new Users();
        user.setId(1L);
        user.setIsAdmin(false);

        // Create mock seat
        Seats seat = new Seats();
        seat.setId(1L);
        seat.setRow(1);
        seat.setCol(1);
        seat.setSeat(true);
        seat.setOccupied(false);
        seat.setOccupiedDate(null);
        seat.setStoreyId(1L);
        seat.setUserId(null);
        seat.setAdminId(1L);

        // Create mock date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        try {
            date = dateFormat.parse("25.07.2024");
        } catch (ParseException e) {
            fail("Date parsing failed");
        }

        // Mock repository methods
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(seatsRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(seatsRepository.save(any(Seats.class))).thenReturn(seat);

        // Call the method to test
        services.bookSeat(1L, 1L, date);

        // Assertions
        assertNotNull(seat);
        assertEquals(1L, seat.getId());
        assertEquals(1, seat.getRow());
        assertEquals(1, seat.getCol());
        assertEquals(true, seat.getSeat());
        assertEquals(true, seat.getOccupied());
        assertEquals(date, seat.getOccupiedDate());
        assertEquals(1L, seat.getStoreyId());
        assertEquals(1L, seat.getUserId());
        assertEquals(1L, seat.getAdminId());

        // Verify interactions with repositories
        verify(usersRepository, times(1)).findById(1L);
        verify(seatsRepository, times(1)).findById(1L);
        verify(seatsRepository, times(1)).save(any(Seats.class));
    }

    @Test
    void testUpdateBooking() {
        // Create mock seat
        Seats seat = new Seats();
        seat.setId(1L);
        seat.setRow(1);
        seat.setCol(1);
        seat.setSeat(true);
        seat.setOccupied(true);
        seat.setOccupiedDate(new Date());
        seat.setStoreyId(1L);
        seat.setUserId(1L);
        seat.setAdminId(1L);

        // Create mock date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date newDate = null;
        try {
            newDate = dateFormat.parse("26.07.2024");
        } catch (ParseException e) {
            fail("Date parsing failed");
        }

        // Mock repository methods
        when(seatsRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(seatsRepository.save(any(Seats.class))).thenReturn(seat);

        // Call the method to test
        services.updateBooking(1L, 1L, newDate);

        // Assertions
        assertNotNull(seat);
        assertEquals(1L, seat.getId());
        assertEquals(1, seat.getRow());
        assertEquals(1, seat.getCol());
        assertEquals(true, seat.getSeat());
        assertEquals(true, seat.getOccupied());
        assertEquals(newDate, seat.getOccupiedDate());
        assertEquals(1L, seat.getStoreyId());
        assertEquals(1L, seat.getUserId());
        assertEquals(1L, seat.getAdminId());

        // Verify interactions with repositories
        verify(seatsRepository, times(1)).findById(1L);
        verify(seatsRepository, times(1)).save(any(Seats.class));
    }

    @Test
    void testCancelBooking() {
        // Create mock seat
        Seats seat = new Seats();
        seat.setId(1L);
        seat.setRow(1);
        seat.setCol(1);
        seat.setSeat(true);
        seat.setOccupied(true);
        seat.setOccupiedDate(new Date());
        seat.setStoreyId(1L);
        seat.setUserId(1L);
        seat.setAdminId(1L);

        // Mock repository methods
        when(seatsRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(seatsRepository.save(any(Seats.class))).thenReturn(seat);

        // Call the method to test
        services.cancelBooking(1L, 1L);

        // Assertions
        assertNotNull(seat);
        assertEquals(1L, seat.getId());
        assertEquals(1, seat.getRow());
        assertEquals(1, seat.getCol());
        assertEquals(true, seat.getSeat());
        assertEquals(false, seat.getOccupied());
        assertNull(seat.getOccupiedDate());
        assertEquals(1L, seat.getStoreyId());
        assertNull(seat.getUserId());
        assertEquals(1L, seat.getAdminId());

        // Verify interactions with repositories
        verify(seatsRepository, times(1)).findById(1L);
        verify(seatsRepository, times(1)).save(any(Seats.class));
    }

    @Test
    void testGetAvailableSeatsInStorey() {
        // Create mock seats
        Seats seat1 = new Seats();
        seat1.setId(1L);
        seat1.setRow(1);
        seat1.setCol(1);
        seat1.setSeat(true);
        seat1.setOccupied(false);
        seat1.setStoreyId(1L);

        Seats seat2 = new Seats();
        seat2.setId(2L);
        seat2.setRow(1);
        seat2.setCol(2);
        seat2.setSeat(true);
        seat2.setOccupied(true);
        seat2.setStoreyId(1L);

        Seats seat3 = new Seats();
        seat3.setId(3L);
        seat3.setRow(2);
        seat3.setCol(1);
        seat3.setSeat(true);
        seat3.setOccupied(false);
        seat3.setStoreyId(1L);

        List<Seats> allSeats = Arrays.asList(seat1, seat2, seat3);

        // Mock repository methods
        when(seatsRepository.findAll()).thenReturn(allSeats);

        // Call the method to test
        List<Seats> availableSeats = services.getAvailableSeatsInStorey(1L, 1L);

        // Assertions
        assertNotNull(availableSeats);
        assertEquals(2, availableSeats.size());
        assertTrue(availableSeats.contains(seat1));
        assertTrue(availableSeats.contains(seat3));

        // Verify interactions with repositories
        verify(seatsRepository, times(1)).findAll();
    }

    @Test
    void testGetAvailableSeatsInBuilding() {
        // Create mock seats
        Seats seat1 = new Seats();
        seat1.setId(1L);
        seat1.setRow(1);
        seat1.setCol(1);
        seat1.setSeat(true);
        seat1.setOccupied(false);
        seat1.setStoreyId(1L);

        Seats seat2 = new Seats();
        seat2.setId(2L);
        seat2.setRow(1);
        seat2.setCol(2);
        seat2.setSeat(true);
        seat2.setOccupied(true);
        seat2.setStoreyId(1L);

        Seats seat3 = new Seats();
        seat3.setId(3L);
        seat3.setRow(2);
        seat3.setCol(1);
        seat3.setSeat(true);
        seat3.setOccupied(false);
        seat3.setStoreyId(2L);

        List<Seats> allSeats = Arrays.asList(seat1, seat2, seat3);

        // Create mock storeys
        Storeys storey1 = new Storeys();
        storey1.setId(1L);
        storey1.setBuildingId(1L);

        Storeys storey2 = new Storeys();
        storey2.setId(2L);
        storey2.setBuildingId(1L);

        // Mock repository methods
        when(seatsRepository.findAll()).thenReturn(allSeats);
        when(storeysRepository.findById(1L)).thenReturn(Optional.of(storey1));
        when(storeysRepository.findById(2L)).thenReturn(Optional.of(storey2));

        // Call the method to test
        List<Seats> availableSeats = services.getAvailableSeatsInBuilding(1L, 1L);

        // Assertions
        assertNotNull(availableSeats);
        assertEquals(2, availableSeats.size());
        assertTrue(availableSeats.contains(seat1));
        assertTrue(availableSeats.contains(seat3));

        // Verify interactions with repositories
        verify(seatsRepository, times(1)).findAll();
        verify(storeysRepository, times(1)).findById(1L);
        verify(storeysRepository, times(1)).findById(2L);
    }
}
