package com.ryanmhub.fitnessapp.role.service;

import com.ryanmhub.fitnessapp.role.model.Role;
import com.ryanmhub.fitnessapp.role.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void createRoleIfNotFound(String name){
        Optional<Role> roleOpt = roleRepository.findByName(name);
        if(roleOpt.isEmpty()){
            Role role = new Role.Builder().name(name).build();
            roleRepository.save(role);
        }

    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
