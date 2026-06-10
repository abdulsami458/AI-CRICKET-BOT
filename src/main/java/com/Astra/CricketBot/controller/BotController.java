package com.Astra.CricketBot.controller;

import com.Astra.CricketBot.service.BotService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cricket")
public class BotController {

    private BotService service;


    public BotController(BotService service) {
        this.service = service;
    }

    @GetMapping("/ask")
    public String response(@AuthenticationPrincipal UserDetails user, @RequestParam String query){
        return service.response(user.getUsername(), query);
    }
}
