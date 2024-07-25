package org.seatsurfer;

import org.seatsurfer.domain.Users;
import org.seatsurfer.repositories.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SeatSurferApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeatSurferApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(UsersRepository usersRepository) {
//        return args -> {
//            Users john = new Users("John Doe", true);
//            usersRepository.deleteAll();
//        };
//    }
}
