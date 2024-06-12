package com.ryanmhub.fitnessapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//DAO for the User Entity
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameOrEmail(String username, String email); //Will find user by username or email. Check both iterations to determine it works
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
