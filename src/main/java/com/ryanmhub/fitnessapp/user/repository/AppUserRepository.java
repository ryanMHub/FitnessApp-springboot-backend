package com.ryanmhub.fitnessapp.user.repository;

import com.ryanmhub.fitnessapp.user.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//DAO for the User Entity
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    //tODO: Maybe remove trial
    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<AppUser> findByIdWithRoles(@Param("id") Long id);

    Optional<AppUser> findByUsernameOrEmail(String username, String email); //Will find user by username or email. Check both iterations to determine it works
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
