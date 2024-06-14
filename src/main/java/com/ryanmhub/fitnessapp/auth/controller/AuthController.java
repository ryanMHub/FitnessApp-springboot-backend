package com.ryanmhub.fitnessapp.auth.controller;

import com.ryanmhub.fitnessapp.auth.service.AuthService;
import com.ryanmhub.fitnessapp.auth.dto.LoginDTO;
import com.ryanmhub.fitnessapp.common.models.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ApiResponse authenticateUser(@Valid @RequestBody LoginDTO loginDTO){
        return authService.authenticateUser(loginDTO);
    }
}
