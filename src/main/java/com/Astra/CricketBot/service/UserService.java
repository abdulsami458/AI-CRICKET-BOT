package com.Astra.CricketBot.service;


import com.Astra.CricketBot.Enum.Role;
import com.Astra.CricketBot.dto.LoginRequest;
import com.Astra.CricketBot.dto.RegisterRequest;
import com.Astra.CricketBot.model.Users;
import com.Astra.CricketBot.model.VerificationToken;
import com.Astra.CricketBot.repo.UsersRepo;
import com.Astra.CricketBot.repo.VerificationRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    JavaMailSender mailSender;
    @Autowired
    VerificationRepo verificationRepo;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Value("${admin.email}")
    private String  email;

    @Value("${admin.password}")
    private String password;

    @Value("${admin.name}")
    private String name;

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public String register(RegisterRequest request){
        if (usersRepo.findByUserName(request.getUserName()).isPresent()){
            throw new RuntimeException("Username exists already");
        }
        Users users = new Users();
        users.setUserName(request.getUserName());
        users.setEmail(request.getEmail());
        users.setRole(Role.ROLE_USER);
        users.setVerified(false);
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        usersRepo.save(users);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setOtp(token);
        verificationToken.setUsers(users);
        verificationToken.setExpiryTime(
                LocalDateTime.now().plusMinutes(5)
        );
        verificationRepo.save(verificationToken);
        String verifyLink = "http://localhost:8080/verify?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(users.getEmail());
        message.setSubject("Email Verification");
        message.setText(verifyLink);
        mailSender.send(message);
        return "Verification Code Sent";
    }

    public String login(LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );
        Users users = usersRepo.findByUserName(request.getUserName()).orElseThrow(()->new RuntimeException("User Not Found"));
        if(users == null || !users.isVerified()){
            throw new RuntimeException("Verify Email First");
        }

            return jwtService.genToken(request.getUserName());
    }

    @Bean
    CommandLineRunner init(){
        return args -> {
            if (usersRepo.findByEmail(email).isEmpty()){
                Users admin = new Users();
                admin.setUserName(name);
                admin.setEmail(email);
                admin.setPassword(passwordEncoder.encode(password));
                admin.setRole(Role.ROLE_ADMIN);
                usersRepo.save(admin);
            }
        };
    }

    public List<Users> getAll(){
        return usersRepo.findAll();
    }
}
