package org.seatsurfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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


// Todo: singular naming for the domain classes, citit despre lombok, cleanup cu lombok, repository -> , @Autowired -> @RequiredArgsConstructor, de citit articolele si despre mapari, lazy/eager initialisation, de citit de seqeunce,