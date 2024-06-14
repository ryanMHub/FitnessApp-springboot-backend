package com.ryanmhub.fitnessapp.user.service;

import com.ryanmhub.fitnessapp.common.models.ApiResponse;
import com.ryanmhub.fitnessapp.user.dto.AppUserDTO;
import com.ryanmhub.fitnessapp.user.model.AppUser;
import com.ryanmhub.fitnessapp.user.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //**********************I need to add a way to determine the roles that users will have*********************************
    public ApiResponse registerUser(AppUserDTO userDTO){
        if(userRepository.existsByUsername(userDTO.getUsername())) {
            return new ApiResponse(false, "Username is already taken!");
        }

        if(userRepository.existsByEmail(userDTO.getEmail())) {
            return new ApiResponse(false, "Email Address already in use!");
        }
        //*********************How am I going to implant the required roles*********************************
        AppUser user = new AppUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getUsername(), userDTO.getEmail(), passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);

        return new ApiResponse(true, "User registered successfully");
    }

    public Optional<AppUser> findByUsernameOrEmail(String usernameOrEmail){
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        AppUser user = findByUsernameOrEmail(usernameOrEmail).orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));
        String[] roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(roles) //Figure out a way to modify roles most appropriately
                .build();
    }
}
