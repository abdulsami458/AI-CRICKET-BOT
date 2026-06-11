package com.Astra.CricketBot.repo;

import com.Astra.CricketBot.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepo extends JpaRepository<VerificationToken,Long> {
Optional<VerificationToken>findByOtp(String otp);
}
