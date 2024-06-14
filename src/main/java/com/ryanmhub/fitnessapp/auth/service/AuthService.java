package com.ryanmhub.fitnessapp.auth.service;

import com.ryanmhub.fitnessapp.auth.dto.LoginDTO;
import com.ryanmhub.fitnessapp.auth.jwt.JwtUtils;
import com.ryanmhub.fitnessapp.common.models.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

//*******************How do we safe guard the app so only logged in users can access functionality of the site. And how does the application know if a user is logged in or not. Through IP, Mac address, Chached data******************************
@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public ApiResponse authenticateUser(LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        return new ApiResponse(true, jwt);
    }


}
