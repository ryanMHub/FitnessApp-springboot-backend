package com.ryanmhub.fitnessapp.user.controller;

import com.ryanmhub.fitnessapp.common.models.ApiResponse;
import com.ryanmhub.fitnessapp.user.service.UserService;
import com.ryanmhub.fitnessapp.user.dto.AppUserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AppUserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse registerUser(@Valid @RequestBody AppUserDTO userDTO){
        return userService.registerUser(userDTO);
    }
}
