package com.ryanmhub.fitnessapp.auth.controller;

import com.ryanmhub.fitnessapp.auth.service.AuthService;
import com.ryanmhub.fitnessapp.auth.dto.LoginDTO;
import com.ryanmhub.fitnessapp.common.response.ApiResponse;
import com.ryanmhub.fitnessapp.config.LogoutService;
import com.ryanmhub.fitnessapp.user.dto.AppUserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService, LogoutService logoutService){
        this.authService = authService;
    }

    //Login endpoint
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(@Valid @RequestBody LoginDTO loginDTO){
        return authService.authenticateUser(loginDTO);
    }

    //Register new user endpoint
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody AppUserDTO userDTO){
        System.out.println("/register Received request from client");
        return authService.registerUser(userDTO);
    }

    //Allows Client to Refresh tokens
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        System.out.println("/refresh-token Received request from client");
        return authService.refreshToken(request, response);
    }


}
