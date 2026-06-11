package com.Astra.CricketBot.controller;

import com.Astra.CricketBot.dto.LoginRequest;
import com.Astra.CricketBot.dto.RegisterRequest;
import com.Astra.CricketBot.model.Users;
import com.Astra.CricketBot.service.JwtService;
import com.Astra.CricketBot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest users){
        userService.register(users);
        return ResponseEntity.ok("User Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request){
        String token = userService.login(request);
        return ResponseEntity.ok(token);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getUsers")
    public List<Users> getAll(){
        return userService.getAll();
    }
}
