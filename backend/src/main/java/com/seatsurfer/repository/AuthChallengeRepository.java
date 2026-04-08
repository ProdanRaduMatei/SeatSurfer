package com.seatsurfer.repository;

import com.seatsurfer.entity.AuthChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthChallengeRepository extends JpaRepository<AuthChallenge, String> {
}
