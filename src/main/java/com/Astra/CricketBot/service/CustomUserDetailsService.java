package com.Astra.CricketBot.service;

import com.Astra.CricketBot.UserPrincipal.UserPrincipal;
import com.Astra.CricketBot.model.Users;
import com.Astra.CricketBot.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Users user = usersRepo.findByUserName(userName).orElseThrow(
                ()-> new UsernameNotFoundException("User Not Found")
        );
        return new UserPrincipal(user);
    }
}
