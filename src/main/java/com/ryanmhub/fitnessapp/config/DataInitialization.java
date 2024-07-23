package com.ryanmhub.fitnessapp.config;

import com.ryanmhub.fitnessapp.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitialization {

    private final RoleService roleService;

    public DataInitialization(RoleService roleService){
        this.roleService = roleService;
    }

    @Bean
    CommandLineRunner init(){
        return args -> {
            roleService.createRoleIfNotFound("GUEST");
            roleService.createRoleIfNotFound("USER");
            roleService.createRoleIfNotFound("ADMIN");
        };
    }
}
