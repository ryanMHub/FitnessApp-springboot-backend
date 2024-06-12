package com.ryanmhub.fitnessapp.auth;

import com.ryanmhub.fitnessapp.common.ApiResponse;
import com.ryanmhub.fitnessapp.user.User;
import com.ryanmhub.fitnessapp.user.UserDTO;
import com.ryanmhub.fitnessapp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//*******************How do we safe guard the app so only logged in users can access functionality of the site. And how does the application know if a user is logged in or not. Through IP, Mac address, Chached data******************************
@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    public ApiResponse authenticateUser(LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ApiResponse(true, "User authenticated successfully");
    }


}
