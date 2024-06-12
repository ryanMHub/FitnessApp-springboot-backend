package com.ryanmhub.fitnessapp.auth;

import com.ryanmhub.fitnessapp.common.ApiResponse;
import com.ryanmhub.fitnessapp.user.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
