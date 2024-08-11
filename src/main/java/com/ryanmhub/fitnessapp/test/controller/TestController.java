package com.ryanmhub.fitnessapp.test.controller;

import com.ryanmhub.fitnessapp.common.response.ApiResponse;
import com.ryanmhub.fitnessapp.test.service.TestService;
import com.ryanmhub.fitnessapp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/print")
    public ResponseEntity<ApiResponse> displaySomething(Authentication authentication){
        return testService.getUserData(authentication.getName());
    }
}
