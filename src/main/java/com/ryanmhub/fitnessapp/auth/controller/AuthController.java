package com.ryanmhub.fitnessapp.auth.controller;

import com.ryanmhub.fitnessapp.auth.service.AuthService;
import com.ryanmhub.fitnessapp.auth.dto.LoginDTO;
import com.ryanmhub.fitnessapp.common.response.ApiResponse;
import com.ryanmhub.fitnessapp.config.LogoutService;
import com.ryanmhub.fitnessapp.config.jwt.LogoutResponse;
import com.ryanmhub.fitnessapp.user.dto.AppUserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;
    private final LogoutService logoutService;

    public AuthController(AuthService authService, LogoutService logoutService){
        this.authService = authService;
        this.logoutService = logoutService;

    }

    //Todo: This response needs to be routed to the client
    //Todo: Switch the ResponseEntity to ApiResponse and have all api responses extend that class
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

    //Logout Client
    //todo: should this have its own controller so a different service file isn't being called
    //todo: Not doing anything
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logoutUser( ){
        System.out.println("/logout Received request from client");
        return new ResponseEntity<>(LogoutResponse.builder().success(true).message("Logout User").build(), HttpStatus.OK);
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
