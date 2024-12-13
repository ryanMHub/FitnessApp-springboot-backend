package com.ryanmhub.fitnessapp.user.service;

import com.ryanmhub.fitnessapp.user.model.AppUser;
import com.ryanmhub.fitnessapp.user.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final AppUserRepository userRepository;

    public UserService(AppUserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<AppUser> findByUsernameOrEmail(String usernameOrEmail){
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    //checks if username is in repository
    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    //checks if email is in repository
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    //saves user to repository
    public AppUser saveUser(AppUser user){
        return userRepository.save(user);
    }

    //Not Currently Using
//    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
//        AppUser user = findByUsernameOrEmail(usernameOrEmail).orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));
//        String[] roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
//        return org.springframework.security.core.userdetails.User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .roles(roles)
//                .build();
//    }
}
