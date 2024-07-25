package com.ryanmhub.fitnessapp.role.repository;

import com.ryanmhub.fitnessapp.role.model.UserRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @EntityGraph(attributePaths = {"role"})
    @Query("select ur from UserRole ur where ur.user.id = :userId")
    List<UserRole> findByUserId(Long userId);
}
