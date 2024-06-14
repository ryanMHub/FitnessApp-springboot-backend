package com.ryanmhub.fitnessapp.user.repository;

import com.ryanmhub.fitnessapp.user.model.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRoleRepository extends JpaRepository<AppUserRole, Long> {
    List<AppUserRole> findByUsername(String username);
}
