//package org.seatsurfer;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.seatsurfer.domain.Building;
//import org.seatsurfer.domain.Seat;
//import org.seatsurfer.domain.Storey;
//import org.seatsurfer.domain.User;
//import org.seatsurfer.persistence.BuildingRepository;
//import org.seatsurfer.persistence.SeatRepository;
//import org.seatsurfer.persistence.StoreyRepository;
//import org.seatsurfer.persistence.UserRepository;
//import org.seatsurfer.service.*;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class SeatSurferApplicationTests {
//
//    @Test
//    void contextLoads() {
//    }
//
//    // Test the User class
//    @Test
//    void testUserClass() {
//        // Create a new User instance
//        User user = new User(1L, "John Doe", true);
//
//        // Test initial values
//        assertEquals(1L, user.getId());
//        assertEquals("John Doe", user.getName());
//        assertTrue(user.getIsAdmin());
//
//        // Modify the User instance
//        user.setName("Jane Doe");
//        user.setIsAdmin(false);
//
//        // Test modified values
//        assertEquals("Jane Doe", user.getName());
//        assertFalse(user.getIsAdmin());
//
//        // Test nullifying the User instance
//        user = null;
//        assertNull(user);
//    }
//
//    // Test the Building class
//    @Test
//    void testBuildings() {
//        Building buildings = new Building();
//        buildings.setId(1L);
//        assertEquals(1L, buildings.getId());
//        buildings.setId(2L);
//        assertEquals(2L, buildings.getId());
//        buildings = null;
//        assertNull(buildings);
//    }
//
//    // Test the Storeys class
//    @Test
//    void testStoreys() {
//        Storey storeys = new Storey(1L, new Building(), new ArrayList<>());
//        storeys.setBuilding(new Building());
//        assertEquals(new Building(), storeys.getBuilding());
//        storeys = null;
//        assertNull(storeys);
//    }
//
//    // Test the Seats class
//    @Test
//    void testSeats() {
//        Seat seats = new Seat();
//        seats.setId(1L);
//        assertEquals(1L, seats.getId());
//        seats.setRow(1);
//        assertEquals(1, seats.getRow());
//        seats.setCol(1);
//        assertEquals(1, seats.getCol());
//        seats.setIsSeat(true);
//        assertEquals(true, seats.getIsSeat());
//        seats.setIsOccupied(true);
//        assertEquals(true, seats.getIsOccupied());
//        seats.setOccupiedDate(null);
//        assertNull(seats.getOccupiedDate());
//        seats.setStorey(new Storey());
//        assertEquals(new Storey(), seats.getStorey());
//        seats.setUser(new User());
//        assertEquals(new User(), seats.getUser());
//        seats = null;
//        assertNull(seats);
//    }
//
//    // Test the User services
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private SeatRepository seatsRepository;
//
//    @Mock
//    private StoreyRepository storeysRepository;
//
//    @Mock
//    private BuildingRepository buildingsRepository;
//
//    @InjectMocks
//    private UserService userService;
//
//    @InjectMocks
//    private SeatService seatService;
//
//    @InjectMocks
//    private StoreyService storeyService;
//
//    @InjectMocks
//    private BuildingService buildingService;
//
//    @InjectMocks
//    private Service service;
//
//    @InjectMocks
//    private SeatSurferApplicationTests seatSurferApplicationTests;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateUser() {
//        User user = new User();
//        user.setId(1L);
//        user.setName("John Doe");
//        user.setIsAdmin(true);
//
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        User createdUser = userService.createUser(user);
//
//        assertNotNull(createdUser, "The created user should not be null");
//        assertEquals(1L, createdUser.getId());
//        assertEquals("John Doe", createdUser.getName());
//        assertTrue(createdUser.getIsAdmin());
//    }
//
//    @Test
//    void testGetUser() {
//        User user = new User();
//        user.setId(1L);
//        user.setName("John Doe");
//        user.setIsAdmin(true);
//
//        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
//
//        Optional<User> foundUser = userService.getUserById(1L);
//
//        assertEquals("John Doe", foundUser.get().getName());
//        assertTrue(foundUser.get().getIsAdmin());
//    }
//
//    @Test
//    void testGetUsers() {
//        User user = new User();
//        user.setId(1L);
//        user.setName("John Doe");
//        user.setIsAdmin(true);
//
//        when(userRepository.findAll()).thenReturn(java.util.List.of(user));
//
//        List<User> foundUsers = userService.getAllUsers();
//
//        assertNotNull(foundUsers);
//        assertEquals(1, foundUsers.size());
//        assertEquals("John Doe", foundUsers.get(0).getName());
//        assertTrue(foundUsers.get(0).getIsAdmin());
//    }
//
//    @Test
//    void testUpdateUser() {
//        User oldUser = new User();
//        oldUser.setId(1L);
//        oldUser.setName("John Doe");
//        oldUser.setIsAdmin(true);
//
//        User updatedUser = new User();
//        updatedUser.setId(1L);
//        updatedUser.setName("Jane Doe");
//        updatedUser.setIsAdmin(false);
//
//        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(oldUser));
//        when(userRepository.save(oldUser)).thenReturn(oldUser);
//
//        userService.updateUser(1L, updatedUser);
//
//        assertNotNull(oldUser);
//        assertEquals("Jane Doe", updatedUser.getName());
//        assertFalse(updatedUser.getIsAdmin());
//    }
//
//    @Test
//    void testDeleteUser() {
//        doNothing().when(userRepository).deleteById(1L);
//
//        userService.deleteUser(1L);
//
//        verify(userRepository, times(1)).deleteById(1L);
//    }
//
//    // Test the Buildings services
//    @Test
//    void testCreateBuilding() {
//        Building building = new Building();
//        building.setId(1L);
//
//        when(buildingsRepository.save(any(Building.class))).thenReturn(building);
//
//        Building createdBuilding = buildingService.createBuilding(building);
//
//        assertNotNull(createdBuilding);
//        assertEquals(1L, createdBuilding.getId());
//    }
//
//    @Test
//    void testGetBuilding() {
//        Building building = new Building();
//        building.setId(1L);
//
//        when(buildingsRepository.findById(1L)).thenReturn(java.util.Optional.of(building));
//
//        Optional<Building> foundBuilding = buildingService.getBuildingById(1L);
//
//        assertNotNull(foundBuilding.isPresent());
//        assertEquals(1L, foundBuilding.get().getId());
//    }
//
//    @Test
//    void testGetBuildings() {
//        Building building = new Building();
//        building.setId(1L);
//
//        when(buildingsRepository.findAll()).thenReturn(java.util.List.of(building));
//
//        List<Building> foundBuildings = buildingService.getAllBuildings();
//
//        assertNotNull(foundBuildings);
//        assertEquals(1, foundBuildings.size());
//        assertEquals(1L, foundBuildings.get(0).getId());
//    }
//
//    @Test
//    void testDeleteBuilding() {
//        doNothing().when(buildingsRepository).deleteById(1L);
//
//        buildingService.deleteBuilding(1L);
//
//        verify(buildingsRepository, times(1)).deleteById(1L);
//    }
//
//    // Test the Storeys services
//    @Test
//    void testCreateStorey() {
//        Storey storey = new Storey();
//        storey.setId(1L);
//        storey.setBuilding(new Building());
//
//        when(storeysRepository.save(storey)).thenReturn(storey);
//
//        Storey createdStorey = storeyService.createStorey(storey);
//
//        assertNotNull(createdStorey);
//        assertEquals(1L, createdStorey.getId());
//        assertEquals(1L, createdStorey.getBuilding().getId());
//    }
//
//    @Test
//    void testGetStorey() {
//        Storey storey = new Storey();
//        storey.setId(1L);
//        storey.setBuilding(new Building());
//
//        when(storeysRepository.findById(1L)).thenReturn(java.util.Optional.of(storey));
//
//        Optional<Storey> foundStorey = storeyService.getStoreyById(1L);
//
//        assertNotNull(foundStorey.isPresent());
//        assertEquals(1L, foundStorey.get().getId());
//        assertEquals(1L, foundStorey.get().getBuilding().getId());
//    }
//
//    @Test
//    void testGetStoreys() {
//        Storey storey = new Storey();
//        storey.setId(1L);
//        storey.setBuilding(new Building());
//
//        when(storeysRepository.findAll()).thenReturn(java.util.List.of(storey));
//
//        List<Storey> foundStoreys = storeyService.getAllStoreys();
//
//        assertNotNull(foundStoreys);
//        assertEquals(1, foundStoreys.size());
//        assertEquals(1L, foundStoreys.get(0).getId());
//        assertEquals(1L, foundStoreys.get(0).getBuilding().getId());
//    }
//
//    @Test
//    void testUpdateStorey() {
//        Storey oldStorey = new Storey();
//        oldStorey.setId(1L);
//        oldStorey.setBuilding(new Building());
//
//        Storey updatedStorey = new Storey();
//        updatedStorey.setId(1L);
//        updatedStorey.setBuilding(new Building());
//
//        when(storeysRepository.findById(1L)).thenReturn(java.util.Optional.of(oldStorey));
//        when(storeysRepository.save(oldStorey)).thenReturn(oldStorey);
//
//        storeyService.updateStorey(1L, updatedStorey);
//
//        assertNotNull(oldStorey);
//        assertEquals(1L, updatedStorey.getId());
//        assertEquals(2L, updatedStorey.getBuilding().getId());
//    }
//
//    @Test
//    void testDeleteStorey() {
//        doNothing().when(storeysRepository).deleteById(1L);
//
//        storeyService.deleteStorey(1L);
//
//        verify(storeysRepository, times(1)).deleteById(1L);
//    }
//
//    // Test the Seats services
//    @Test
//    void testCreateSeat() {
//        Seat seat = new Seat();
//        seat.setId(1L);
//        seat.setRow(1);
//        seat.setCol(1);
//        seat.setIsSeat(true);
//        seat.setIsOccupied(true);
//        seat.setOccupiedDate(null);
//        seat.setStorey(new Storey());
//        seat.setUser(new User());
//        seat.setAdmin(new User());
//
//        when(seatsRepository.save(any(Seat.class))).thenReturn(seat);
//
//        Seat createdSeat = seatService.createSeat(seat);
//
//        assertNotNull(createdSeat);
//        assertEquals(1L, createdSeat.getId());
//        assertEquals(1, createdSeat.getRow());
//        assertEquals(1, createdSeat.getCol());
//        assertEquals(true, createdSeat.getIsSeat());
//        assertEquals(true, createdSeat.getIsOccupied());
//        assertNull(createdSeat.getOccupiedDate());
//        assertEquals(1L, createdSeat.getStorey().getId());
//        assertEquals(1L, createdSeat.getUser().getId());
//        assertEquals(1L, createdSeat.getAdmin().getId());
//    }
//
//    @Test
//    void testGetSeat() {
//        Seat seat = new Seat();
//        seat.setId(1L);
//        seat.setRow(1);
//        seat.setCol(1);
//        seat.setIsSeat(true);
//        seat.setIsOccupied(true);
//        seat.setOccupiedDate(null);
//        seat.setStorey(new Storey());
//        seat.setUser(new User());
//        seat.setAdmin(new User());
//
//        when(seatsRepository.findById(1L)).thenReturn(Optional.of(seat));
//
//        Optional<Seat> foundSeat = seatService.getSeatById(1L);
//
//        assertTrue(foundSeat.isPresent());
//        assertEquals(1L, foundSeat.get().getId());
//        assertEquals(1, foundSeat.get().getRow());
//        assertEquals(1, foundSeat.get().getCol());
//        assertEquals(true, foundSeat.get().getIsSeat());
//        assertEquals(true, foundSeat.get().getIsOccupied());
//        assertNull(foundSeat.get().getOccupiedDate());
//        assertEquals(1L, foundSeat.get().getStorey().getId());
//        assertEquals(1L, foundSeat.get().getUser().getId());
//        assertEquals(1L, foundSeat.get().getAdmin().getId());
//    }
//
//    @Test
//    void testGetSeats() {
//        Seat seat = new Seat();
//        seat.setId(1L);
//        seat.setRow(1);
//        seat.setCol(1);
//        seat.setIsSeat(true);
//        seat.setIsOccupied(true);
//        seat.setOccupiedDate(null);
//        seat.setStorey(new Storey());
//        seat.setUser(new User());
//        seat.setAdmin(new User());
//
//        when(seatsRepository.findAll()).thenReturn(List.of(seat));
//
//        List<Seat> foundSeats = seatService.getAllSeats();
//
//        assertNotNull(foundSeats);
//        assertEquals(1, foundSeats.size());
//        assertEquals(1L, foundSeats.get(0).getId());
//        assertEquals(1, foundSeats.get(0).getRow());
//        assertEquals(1, foundSeats.get(0).getCol());
//        assertEquals(true, foundSeats.get(0).getIsSeat());
//        assertEquals(true, foundSeats.get(0).getIsOccupied());
//        assertNull(foundSeats.get(0).getOccupiedDate());
//        assertEquals(1L, foundSeats.get(0).getStorey().getId());
//        assertEquals(1L, foundSeats.get(0).getUser().getId());
//        assertEquals(1L, foundSeats.get(0).getAdmin().getId());
//    }
//
//    @Test
//    void testUpdateSeat() {
//        Seat oldSeat = new Seat();
//        oldSeat.setId(1L);
//        oldSeat.setRow(1);
//        oldSeat.setCol(1);
//        oldSeat.setIsSeat(true);
//        oldSeat.setIsOccupied(true);
//        oldSeat.setOccupiedDate(null);
//        oldSeat.setStorey(new Storey());
//        oldSeat.setUser(new User());
//        oldSeat.setAdmin(new User());
//
//        Seat updatedSeat = new Seat();
//        updatedSeat.setId(1L);
//        updatedSeat.setRow(2);
//        updatedSeat.setCol(2);
//        updatedSeat.setIsSeat(false);
//        updatedSeat.setIsOccupied(false);
//        updatedSeat.setOccupiedDate(null);
//        updatedSeat.setStorey(new Storey());
//        updatedSeat.setUser(new User());
//        updatedSeat.setAdmin(new User());
//
//        when(seatsRepository.findById(1L)).thenReturn(Optional.of(oldSeat));
//        when(seatsRepository.save(any(Seat.class))).thenReturn(updatedSeat);
//
//        Seat result = seatService.updateSeat(1L, updatedSeat);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//        assertEquals(2, result.getRow());
//        assertEquals(2, result.getCol());
//        assertEquals(false, result.getIsSeat());
//        assertEquals(false, result.getIsOccupied());
//        assertNull(result.getOccupiedDate());
//        assertEquals(2L, result.getStorey().getId());
//        assertEquals(2L, result.getUser().getId());
//        assertEquals(1L, result.getAdmin().getId());
//    }
//
//    @Test
//    void testDeleteSeat() {
//        doNothing().when(seatsRepository).deleteById(1L);
//
//        seatService.deleteSeat(1L);
//
//        verify(seatsRepository, times(1)).deleteById(1L);
//    }
//
//    // Test the Admin services
////    @Test
////    void testAddStorey() {
////        Storey storey = new Storey();
////        storey.setId(1L);
////        storey.setBuilding(new Building());
////
////        User admin = new User();
////        admin.setId(1L);
////        admin.setIsAdmin(true);
////
////        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
////        when(storeysRepository.save(any(Storey.class))).thenReturn(storey);
////
////        Storey result = services.addStorey(1L, 1L);
////
////        assertNotNull(result);
////        assertEquals(1L, result.getId());
////        assertEquals(1L, result.getBuilding().getId());
////    }
////
////    @Test
////    void testEditStorey() {
////        Storey storey = new Storey();
////        storey.setId(1L);
////        storey.setBuilding(new Building());
////
////        User admin = new User();
////        admin.setId(1L);
////        admin.setIsAdmin(true);
////
////        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
////        when(storeysRepository.findById(1L)).thenReturn(Optional.of(storey));
////
////        services.editStorey(1L, 1L, 2L);
////
////        assertNotNull(storey);
////        assertEquals(1L, storey.getId());
////        assertEquals(2L, storey.getBuilding().getId());
////    }
////
////    @Test
////    void testRemoveStorey() {
////        Storey storey = new Storey();
////        storey.setId(1L);
////        storey.setBuilding(new Building());
////
////        User admin = new User();
////        admin.setId(1L);
////        admin.setIsAdmin(true);
////
////        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
////        when(storeysRepository.findById(1L)).thenReturn(Optional.of(storey));
////
////        services.removeStorey(1L, 1L);
////
////        verify(storeysRepository, times(1)).deleteById(1L);
////    }
////
////    @Test
////    void testAddSeat() {
////        User admin = new User();
////        admin.setId(1L);
////        admin.setIsAdmin(true);
////
////        Seat seat = new Seat();
////        seat.setId(1L);
////        seat.setRow(1);
////        seat.setCol(1);
////        seat.setSeat(true);
////        seat.setOccupied(false);
////        seat.setOccupiedDate(null);
////        seat.setStoreyId(1L);
////        seat.setUser(null);
////        seat.setAdmin(1L);
////
////        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
////        when(seatsRepository.save(any(Seat.class))).thenReturn(seat);
////
////        Seat result = services.addSeat(1L, 1L, 1, 1, true, false, null);
////
////        assertNotNull(result);
////        assertEquals(1L, result.getId());
////        assertEquals(1, result.getRow());
////        assertEquals(1, result.getCol());
////        assertEquals(true, result.getSeat());
////        assertEquals(false, result.getOccupied());
////        assertNull(result.getOccupiedDate());
////        assertEquals(1L, result.getStoreyId());
////        assertNull(result.getUser());
////        assertEquals(1L, result.getAdmin());
////
////        verify(userRepository, times(1)).findById(1L);
////        verify(seatsRepository, times(1)).save(any(Seat.class));
////    }
////
////    @Test
////    void testEditSeat() {
////        User admin = new User();
////        admin.setId(1L);
////        admin.setIsAdmin(true);
////
////        Seat seat = new Seat();
////        seat.setId(1L);
////        seat.setRow(1);
////        seat.setCol(1);
////        seat.setSeat(true);
////        seat.setOccupied(false);
////        seat.setOccupiedDate(null);
////        seat.setStoreyId(1L);
////        seat.setUser(null);
////        seat.setAdmin(1L);
////
////        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
////        when(seatsRepository.findById(1L)).thenReturn(Optional.of(seat));
////        when(seatsRepository.save(any(Seat.class))).thenReturn(seat);
////
////        services.editSeat(1L, 1L, 1L, 2, 2, false, false, null, 1L);
////
////        assertNotNull(seat);
////        assertEquals(1L, seat.getId());
////        assertEquals(2, seat.getRow());
////        assertEquals(2, seat.getCol());
////        assertEquals(false, seat.getSeat());
////        assertEquals(false, seat.getOccupied());
////        assertNull(seat.getOccupiedDate());
////        assertEquals(1L, seat.getStoreyId());
////        assertEquals(null, seat.getUser());
////        assertEquals(1L, seat.getAdmin());
////
////        verify(userRepository, times(1)).findById(1L);
////        verify(seatsRepository, times(1)).findById(1L);
////        verify(seatsRepository, times(1)).save(any(Seat.class)); // Ensure the save method is verified
////    }
////
////    @Test
////    void testRemoveSeat() {
////        User admin = new User();
////        admin.setId(1L);
////        admin.setIsAdmin(true);
////
////        Seat seat = new Seat();
////        seat.setId(1L);
////        seat.setRow(1);
////        seat.setCol(1);
////        seat.setSeat(true);
////        seat.setOccupied(false);
////        seat.setOccupiedDate(null);
////        seat.setStoreyId(1L);
////        seat.setUser(null);
////        seat.setAdmin(1L);
////
////        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
////        when(seatsRepository.findById(1L)).thenReturn(Optional.of(seat));
////
////        services.removeSeat(1L, 1L);
////
////        verify(seatsRepository, times(1)).deleteById(1L);
////    }
////
////    @Test
////    void testGetBookedSeatsInStorey() {
////        User admin = new User();
////        admin.setId(1L);
////        admin.setIsAdmin(true);
////
////        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
////        Date date = null;
////        try {
////            date = dateFormat.parse("25.07.2024");
////        } catch (ParseException e) {
////            fail("Date parsing failed");
////        }
////
////        Seat seat1 = new Seat();
////        seat1.setId(1L);
////        seat1.setRow(1);
////        seat1.setCol(1);
////        seat1.setSeat(true);
////        seat1.setOccupied(true);
////        seat1.setOccupiedDate(date);
////        seat1.setStoreyId(1L);
////        seat1.setUser(1L);
////        seat1.setAdmin(1L);
////
////        Seat seat2 = new Seat();
////        seat2.setId(2L);
////        seat2.setRow(2);
////        seat2.setCol(2);
////        seat2.setSeat(true);
////        seat2.setOccupied(true);
////        seat2.setOccupiedDate(date);
////        seat2.setStoreyId(1L);
////        seat2.setUser(2L);
////        seat2.setAdmin(1L);
////
////        List<Seat> allSeats = List.of(seat1, seat2);
////
////        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
////        when(seatsRepository.findAll()).thenReturn(allSeats);
////
////        List<Seat> bookedSeats = services.getBookedSeatsInStorey(1L, 1L);
////
////        assertNotNull(bookedSeats);
////        assertEquals(2, bookedSeats.size());
////        assertEquals(1L, bookedSeats.get(0).getId());
////        assertEquals(2L, bookedSeats.get(1).getId());
////        assertEquals(1L, bookedSeats.get(0).getStoreyId());
////        assertEquals(1L, bookedSeats.get(1).getStoreyId());
////        assertTrue(bookedSeats.get(0).getOccupied());
////        assertTrue(bookedSeats.get(1).getOccupied());
////        assertEquals(date, bookedSeats.get(0).getOccupiedDate());
////        assertEquals(date, bookedSeats.get(1).getOccupiedDate());
////
////        verify(userRepository, times(1)).findById(1L);
////        verify(seatsRepository, times(1)).findAll();
////    }
////
////    @Test
////    void testGetBookedSeatsInBuilding() {
////        User admin = new User();
////        admin.setId(1L);
////        admin.setIsAdmin(true);
////
////        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
////        Date date = null;
////        try {
////            date = dateFormat.parse("25.07.2024");
////        } catch (ParseException e) {
////            fail("Date parsing failed");
////        }
////
////        Building building = new Building();
////        building.setId(1L);
////
////        Storey storey = new Storey();
////        storey.setId(1L);
////        storey.setBuildingId(1L);
////
////        Seat seat1 = new Seat();
////        seat1.setId(1L);
////        seat1.setRow(1);
////        seat1.setCol(1);
////        seat1.setSeat(true);
////        seat1.setOccupied(true);
////        seat1.setOccupiedDate(date);
////        seat1.setStoreyId(1L);
////        seat1.setUser(1L);
////        seat1.setAdmin(1L);
////
////        Seat seat2 = new Seat();
////        seat2.setId(2L);
////        seat2.setRow(2);
////        seat2.setCol(2);
////        seat2.setSeat(true);
////        seat2.setOccupied(true);
////        seat2.setOccupiedDate(date);
////        seat2.setStoreyId(1L);
////        seat2.setUser(2L);
////        seat2.setAdmin(1L);
////
////        List<Seat> allSeats = List.of(seat1, seat2);
////
////        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
////        when(seatsRepository.findAll()).thenReturn(allSeats);
////        when(storeysRepository.findById(1L)).thenReturn(Optional.of(storey));
////
////        List<Seat> bookedSeats = services.getBookedSeatsInBuilding(1L, 1L);
////
////        assertNotNull(bookedSeats);
////        assertEquals(2, bookedSeats.size());
////        assertEquals(1L, bookedSeats.get(0).getId());
////        assertEquals(2L, bookedSeats.get(1).getId());
////        assertEquals(1L, bookedSeats.get(0).getStoreyId());
////        assertEquals(1L, bookedSeats.get(1).getStoreyId());
////        assertTrue(bookedSeats.get(0).getOccupied());
////        assertTrue(bookedSeats.get(1).getOccupied());
////        assertEquals(date, bookedSeats.get(0).getOccupiedDate());
////        assertEquals(date, bookedSeats.get(1).getOccupiedDate());
////
////        verify(userRepository, times(1)).findById(1L);
////        verify(seatsRepository, times(1)).findAll();
////        verify(storeysRepository, times(1)).findById(1L);
////    }
////
////    @Test
////    void testBookSeat() {
////        // Create mock user
////        User user = new User();
////        user.setId(1L);
////        user.setIsAdmin(false);
////
////        // Create mock seat
////        Seat seat = new Seat();
////        seat.setId(1L);
////        seat.setRow(1);
////        seat.setCol(1);
////        seat.setSeat(true);
////        seat.setOccupied(false);
////        seat.setOccupiedDate(null);
////        seat.setStoreyId(1L);
////        seat.setUser(null);
////        seat.setAdmin(1L);
////
////        // Create mock date
////        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
////        Date date = null;
////        try {
////            date = dateFormat.parse("25.07.2024");
////        } catch (ParseException e) {
////            fail("Date parsing failed");
////        }
////
////        // Mock repository methods
////        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
////        when(seatsRepository.findById(1L)).thenReturn(Optional.of(seat));
////        when(seatsRepository.save(any(Seat.class))).thenReturn(seat);
////
////        // Call the method to test
////        services.bookSeat(1L, 1L, date);
////
////        // Assertions
////        assertNotNull(seat);
////        assertEquals(1L, seat.getId());
////        assertEquals(1, seat.getRow());
////        assertEquals(1, seat.getCol());
////        assertEquals(true, seat.getSeat());
////        assertEquals(true, seat.getOccupied());
////        assertEquals(date, seat.getOccupiedDate());
////        assertEquals(1L, seat.getStoreyId());
////        assertEquals(1L, seat.getUser());
////        assertEquals(1L, seat.getAdmin());
////
////        // Verify interactions with repositories
////        verify(userRepository, times(1)).findById(1L);
////        verify(seatsRepository, times(1)).findById(1L);
////        verify(seatsRepository, times(1)).save(any(Seat.class));
////    }
////
////    @Test
////    void testUpdateBooking() {
////        // Create mock seat
////        Seat seat = new Seat();
////        seat.setId(1L);
////        seat.setRow(1);
////        seat.setCol(1);
////        seat.setSeat(true);
////        seat.setOccupied(true);
////        seat.setOccupiedDate(new Date());
////        seat.setStoreyId(1L);
////        seat.setUser(1L);
////        seat.setAdmin(1L);
////
////        // Create mock date
////        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
////        Date newDate = null;
////        try {
////            newDate = dateFormat.parse("26.07.2024");
////        } catch (ParseException e) {
////            fail("Date parsing failed");
////        }
////
////        // Mock repository methods
////        when(seatsRepository.findById(1L)).thenReturn(Optional.of(seat));
////        when(seatsRepository.save(any(Seat.class))).thenReturn(seat);
////
////        // Call the method to test
////        services.updateBooking(1L, 1L, newDate);
////
////        // Assertions
////        assertNotNull(seat);
////        assertEquals(1L, seat.getId());
////        assertEquals(1, seat.getRow());
////        assertEquals(1, seat.getCol());
////        assertEquals(true, seat.getSeat());
////        assertEquals(true, seat.getOccupied());
////        assertEquals(newDate, seat.getOccupiedDate());
////        assertEquals(1L, seat.getStoreyId());
////        assertEquals(1L, seat.getUser());
////        assertEquals(1L, seat.getAdmin());
////
////        // Verify interactions with repositories
////        verify(seatsRepository, times(1)).findById(1L);
////        verify(seatsRepository, times(1)).save(any(Seat.class));
////    }
////
////    @Test
////    void testCancelBooking() {
////        // Create mock seat
////        Seat seat = new Seat();
////        seat.setId(1L);
////        seat.setRow(1);
////        seat.setCol(1);
////        seat.setSeat(true);
////        seat.setOccupied(true);
////        seat.setOccupiedDate(new Date());
////        seat.setStoreyId(1L);
////        seat.setUser(1L);
////        seat.setAdmin(1L);
////
////        // Mock repository methods
////        when(seatsRepository.findById(1L)).thenReturn(Optional.of(seat));
////        when(seatsRepository.save(any(Seat.class))).thenReturn(seat);
////
////        // Call the method to test
////        services.cancelBooking(1L, 1L);
////
////        // Assertions
////        assertNotNull(seat);
////        assertEquals(1L, seat.getId());
////        assertEquals(1, seat.getRow());
////        assertEquals(1, seat.getCol());
////        assertEquals(true, seat.getSeat());
////        assertEquals(false, seat.getOccupied());
////        assertNull(seat.getOccupiedDate());
////        assertEquals(1L, seat.getStoreyId());
////        assertNull(seat.getUser());
////        assertEquals(1L, seat.getAdmin());
////
////        // Verify interactions with repositories
////        verify(seatsRepository, times(1)).findById(1L);
////        verify(seatsRepository, times(1)).save(any(Seat.class));
////    }
////
////    @Test
////    void testGetAvailableSeatsInStorey() {
////        // Create mock seats
////        Seat seat1 = new Seat();
////        seat1.setId(1L);
////        seat1.setRow(1);
////        seat1.setCol(1);
////        seat1.setSeat(true);
////        seat1.setOccupied(false);
////        seat1.setStoreyId(1L);
////
////        Seat seat2 = new Seat();
////        seat2.setId(2L);
////        seat2.setRow(1);
////        seat2.setCol(2);
////        seat2.setSeat(true);
////        seat2.setOccupied(true);
////        seat2.setStoreyId(1L);
////
////        Seat seat3 = new Seat();
////        seat3.setId(3L);
////        seat3.setRow(2);
////        seat3.setCol(1);
////        seat3.setSeat(true);
////        seat3.setOccupied(false);
////        seat3.setStoreyId(1L);
////
////        List<Seat> allSeats = Arrays.asList(seat1, seat2, seat3);
////
////        // Mock repository methods
////        when(seatsRepository.findAll()).thenReturn(allSeats);
////
////        // Call the method to test
////        List<Seat> availableSeats = services.getAvailableSeatsInStorey(1L, 1L);
////
////        // Assertions
////        assertNotNull(availableSeats);
////        assertEquals(2, availableSeats.size());
////        assertTrue(availableSeats.contains(seat1));
////        assertTrue(availableSeats.contains(seat3));
////
////        // Verify interactions with repositories
////        verify(seatsRepository, times(1)).findAll();
////    }
////
////    @Test
////    void testGetAvailableSeatsInBuilding() {
////        // Create mock seats
////        Seat seat1 = new Seat();
////        seat1.setId(1L);
////        seat1.setRow(1);
////        seat1.setCol(1);
////        seat1.setSeat(true);
////        seat1.setOccupied(false);
////        seat1.setStoreyId(1L);
////
////        Seat seat2 = new Seat();
////        seat2.setId(2L);
////        seat2.setRow(1);
////        seat2.setCol(2);
////        seat2.setSeat(true);
////        seat2.setOccupied(true);
////        seat2.setStoreyId(1L);
////
////        Seat seat3 = new Seat();
////        seat3.setId(3L);
////        seat3.setRow(2);
////        seat3.setCol(1);
////        seat3.setSeat(true);
////        seat3.setOccupied(false);
////        seat3.setStoreyId(2L);
////
////        List<Seat> allSeats = Arrays.asList(seat1, seat2, seat3);
////
////        // Create mock storeys
////        Storey storey1 = new Storey();
////        storey1.setId(1L);
////        storey1.setBuildingId(1L);
////
////        Storey storey2 = new Storey();
////        storey2.setId(2L);
////        storey2.setBuildingId(1L);
////
////        // Mock repository methods
////        when(seatsRepository.findAll()).thenReturn(allSeats);
////        when(storeysRepository.findById(1L)).thenReturn(Optional.of(storey1));
////        when(storeysRepository.findById(2L)).thenReturn(Optional.of(storey2));
////
////        // Call the method to test
////        List<Seat> availableSeats = services.getAvailableSeatsInBuilding(1L, 1L);
////
////        // Assertions
////        assertNotNull(availableSeats);
////        assertEquals(2, availableSeats.size());
////        assertTrue(availableSeats.contains(seat1));
////        assertTrue(availableSeats.contains(seat3));
////
////        // Verify interactions with repositories
////        verify(seatsRepository, times(1)).findAll();
////        verify(storeysRepository, times(1)).findById(1L);
////        verify(storeysRepository, times(1)).findById(2L);
////    }
//}
