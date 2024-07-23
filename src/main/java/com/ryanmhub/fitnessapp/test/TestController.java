package com.ryanmhub.fitnessapp.test;

import com.ryanmhub.fitnessapp.common.response.ApiResponse;
import com.ryanmhub.fitnessapp.user.model.AppUser;
import com.ryanmhub.fitnessapp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("/print")
    private ApiResponse displaySomething(Authentication authentication){
        String username = authentication.getName();
        AppUser user = userService.findByUsernameOrEmail(username).orElse(null);
        return new ApiResponse(true, user.toString());
    }
}
