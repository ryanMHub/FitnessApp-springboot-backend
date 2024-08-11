package com.ryanmhub.fitnessapp.test.service;

import com.ryanmhub.fitnessapp.common.response.ApiResponse;
import com.ryanmhub.fitnessapp.test.response.UserDataResponse;
import com.ryanmhub.fitnessapp.user.model.AppUser;
import com.ryanmhub.fitnessapp.user.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TestService {

    private final AppUserRepository appUserRepository;

    public TestService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    //get UserData using username or password and return Api response to caller
    public ResponseEntity<ApiResponse> getUserData(String username){
        AppUser user = appUserRepository.findByUsernameOrEmail(username, username).orElse(null);
        if(user == null){
            return new ResponseEntity<>(UserDataResponse.builder()
                    .firstName("")
                    .lastName("")
                    .username("")
                    .email("")
                    .password("")
                    .message("Authorized")
                    .success(true).build(),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(UserDataResponse.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .success(true)
                    .message("Authorized")
                    .build(),
                    HttpStatus.OK);
        }
    }
}
