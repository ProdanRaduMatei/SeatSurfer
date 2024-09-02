package org.seatsurfer;

import org.seatsurfer.domain.*;
import org.seatsurfer.persistence.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SeatSurferApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeatSurferApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(AdminRepository adminRepository, BuildingRepository buildingRepository, StoreyRepository storeyRepository, SeatRepository seatRepository, BookingRepository bookingRepository) {
//        return args -> {
//            //admin
//            Admin admin = new Admin();
////            admin.setId(1L);
//            admin.setName("Admin Name");
//            admin.setEmail("admin@gmail.com");
//            admin.setPassword("password");
// 
//            //buildings
//            Building building1 = new Building();
////            building1.setId(1L);
//            building1.setName("Building Name1");
//            building1.setAdmin(admin);
//
//            Building building2 = new Building();
////            building2.setId(2L);
//            building2.setName("Building Name2");
//            building2.setAdmin(admin);
//
//            //storeys
//            Storey storey1 = new Storey();
////            storey1.setId(1L);
//            storey1.setName("Storey Name1");
//            storey1.setBuilding(building1);
//
//            Storey storey2 = new Storey();
////            storey2.setId(2L);
//            storey2.setName("Storey Name2");
//            storey2.setBuilding(building1);
//
//            //seats
//            Seat seat1 = new Seat();
////            seat1.setId(1L);
//            seat1.setLine(1);
//            seat1.setCol(1);
//            seat1.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat1.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat1.setSeatType("Standing");
//            seat1.setStorey(storey1);
//
//            Seat seat2 = new Seat();
////            seat2.setId(2L);
//            seat2.setLine(1);
//            seat2.setCol(2);
//            seat2.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat2.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat2.setSeatType("2 Monitors");
//            seat2.setStorey(storey1);
//
//            Seat seat3 = new Seat();
////            seat3.setId(3L);
//            seat3.setLine(2);
//            seat3.setCol(1);
//            seat3.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat3.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat3.setSeatType("1 Monitor");
//            seat3.setStorey(storey1);
//
//            Seat seat4 = new Seat();
////            seat4.setId(4L);
//            seat4.setLine(2);
//            seat4.setCol(2);
//            seat4.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat4.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat4.setSeatType("No Monitors");
//            seat4.setStorey(storey1);
//
//            Seat seat5 = new Seat();
////            seat5.setId(5L);
//            seat5.setLine(1);
//            seat5.setCol(1);
//            seat5.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat5.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat5.setSeatType("Standing");
//            seat5.setStorey(storey2);
//
//            Seat seat6 = new Seat();
////            seat6.setId(6L);
//            seat6.setLine(1);
//            seat6.setCol(2);
//            seat6.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat6.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat6.setSeatType("2 Monitors");
//            seat6.setStorey(storey2);
//
//            Seat seat7 = new Seat();
////            seat7.setId(7L);
//            seat7.setLine(2);
//            seat7.setCol(1);
//            seat7.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat7.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat7.setSeatType("1 Monitor");
//            seat7.setStorey(storey2);
//
//            Seat seat8 = new Seat();
////            seat8.setId(8L);
//            seat8.setLine(2);
//            seat8.setCol(2);
//            seat8.setCreationDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat8.setEndAvailabilityDate(Instant.parse("2021-01-01T00:00:00Z"));
//            seat8.setSeatType("No Monitors");
//            seat8.setStorey(storey2);
//
//            //bookings
//            Booking booking1 = new Booking();
////            booking1.setId(1L);
//            booking1.setDate(Instant.parse("2021-01-01T00:00:00Z"));
//            booking1.setSeat(seat1);
//            booking1.setUserName("User Name1");
//            booking1.setEmail("user1@gmail.com");
//
//            Booking booking2 = new Booking();
////            booking2.setId(2L);
//            booking2.setDate(Instant.parse("2021-01-01T00:00:00Z"));
//            booking2.setSeat(seat2);
//            booking2.setUserName("User Name2");
//            booking2.setEmail("user2@gmail.com");
//
//            Booking booking3 = new Booking();
////            booking3.setId(3L);
//            booking3.setDate(Instant.parse("2021-01-01T00:00:00Z"));
//            booking3.setSeat(seat3);
//            booking3.setUserName("User Name3");
//            booking3.setEmail("user3@gmail.com");
//
//            Booking booking4 = new Booking();
////            booking4.setId(4L);
//            booking4.setDate(Instant.parse("2021-01-01T00:00:00Z"));
//            booking4.setSeat(seat1);
//            booking4.setUserName("User Name4");
//            booking4.setEmail("user4@gmail.com");
//
//
//            seat1.setBookings(new ArrayList<>(List.of(booking1, booking4)));
//            seat2.setBookings(new ArrayList<>(List.of(booking2)));
//            seat3.setBookings(new ArrayList<>(List.of(booking3)));
//            storey1.setSeats(new ArrayList<>(List.of(seat1, seat2, seat3, seat4)));
//            storey2.setSeats(new ArrayList<>(List.of(seat5, seat6, seat7, seat8)));
//            building1.setStoreys(new ArrayList<>(List.of(storey1, storey2)));
//            admin.setBuildings(new ArrayList<>(List.of(building1, building2)));
//            adminRepository.save(admin);
//            buildingRepository.saveAll(List.of(building1, building2));
//            storeyRepository.saveAll(List.of(storey1, storey2));
//            seatRepository.saveAll(List.of(seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8));
//            bookingRepository.saveAll(List.of(booking1, booking2, booking3, booking4));
////            bookingRepository.saveAndFlush(booking1);
////            bookingRepository.saveAndFlush(booking2);
////            bookingRepository.saveAndFlush(booking3);
////            bookingRepository.saveAndFlush(booking4);
////            seatRepository.saveAndFlush(seat1);
////            seatRepository.saveAndFlush(seat2);
////            seatRepository.saveAndFlush(seat3);
////            seatRepository.saveAndFlush(seat4);
////            seatRepository.saveAndFlush(seat5);
////            seatRepository.saveAndFlush(seat6);
////            seatRepository.saveAndFlush(seat7);
////            seatRepository.saveAndFlush(seat8);
////            storeyRepository.saveAndFlush(storey1);
////            storeyRepository.saveAndFlush(storey2);
////            buildingRepository.saveAndFlush(building1);
////            buildingRepository.saveAndFlush(building2);
////            adminRepository.saveAndFlush(admin);
//        };
//    }
}


// Todo: singular naming for the domain classes, citit despre lombok, cleanup cu lombok, repository -> , @Autowired -> @RequiredArgsConstructor, de citit articolele si despre mapari, lazy/eager initialisation, de citit de seqeunce; utlity methods, +add, remove;|
// Todo: