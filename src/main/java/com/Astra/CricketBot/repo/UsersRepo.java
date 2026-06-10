package com.Astra.CricketBot.repo;


import com.Astra.CricketBot.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<Users,Long> {
    Optional<Users> findByUserName(String userName);
    Optional<Users> findByEmail(String email);

}
