package com.ryanmhub.fitnessapp.test;

import com.ryanmhub.fitnessapp.common.response.ApiResponse;
import com.ryanmhub.fitnessapp.user.model.AppUser;
import com.ryanmhub.fitnessapp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasRole;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/print")
    public ResponseEntity<ApiResponse> displaySomething(Authentication authentication){
        String username = authentication.getName();
        //AppUser user = userService.findByUsernameOrEmail(username).orElse(null);
        return new ResponseEntity<>(ApiResponse.builder().success(true).message("It worked").build(), HttpStatus.OK);
    }
}
