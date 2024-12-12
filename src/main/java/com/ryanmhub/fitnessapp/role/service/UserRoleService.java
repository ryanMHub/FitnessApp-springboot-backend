package com.ryanmhub.fitnessapp.role.service;
import com.ryanmhub.fitnessapp.role.model.UserRole;
import com.ryanmhub.fitnessapp.role.repository.UserRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

//Controls access and manipulation to the userRole table
@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository){
        this.userRoleRepository = userRoleRepository;
    }

    //Return a list of all the Roles associated with each user by userId
    @Transactional(readOnly = true)
    public List<UserRole> findRolesByUserId(Long userId){
        return userRoleRepository.findByUserId(userId);
    }
}
