package org.seatsurfer.persistence;

import org.seatsurfer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Poți adăuga metode personalizate, ex:
    // Optional<User> findByEmail(String email);
}