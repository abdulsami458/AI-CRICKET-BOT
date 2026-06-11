package com.Astra.CricketBot.service;

import com.Astra.CricketBot.model.Users;
import com.Astra.CricketBot.model.VerificationToken;
import com.Astra.CricketBot.repo.UsersRepo;
import com.Astra.CricketBot.repo.VerificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationService {

    @Autowired
    VerificationRepo verificationRepo;

    @Autowired
    UsersRepo usersRepo;

    public String verify(String token){

        VerificationToken verificationToken = verificationRepo.findByOtp(token).orElseThrow(()->
                new RuntimeException("Invalid Token"));
        if (verificationToken.getExpiryTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Expired Token");
        }
        Users users = verificationToken.getUsers();
        users.setVerified(true);
        usersRepo.save(users);
        verificationRepo.delete(verificationToken);
        return "Email verified";
    }
}
