package com.ryanmhub.fitnessapp.role.service;

import com.ryanmhub.fitnessapp.role.model.Role;
import com.ryanmhub.fitnessapp.role.model.UserRole;
import com.ryanmhub.fitnessapp.role.repository.UserRoleRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//Todo: Probably not needed
@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository){
        this.userRoleRepository = userRoleRepository;
    }

    //Return a list of all of the Roles associated with each user by userId
    @Transactional(readOnly = true)
    public List<UserRole> findRolesByUserId(Long userId){
        return userRoleRepository.findByUserId(userId);
    }
}
