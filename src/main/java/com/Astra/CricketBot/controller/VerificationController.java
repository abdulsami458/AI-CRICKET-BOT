package com.Astra.CricketBot.controller;

import com.Astra.CricketBot.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationController {
    @Autowired
    VerificationService verificationService;

    @GetMapping("/verify")
    public String verify(@RequestParam String token){
        verificationService.verify(token);
        return "Email verified";
    }
}
