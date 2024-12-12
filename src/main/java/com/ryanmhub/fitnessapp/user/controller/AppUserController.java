package com.ryanmhub.fitnessapp.user.controller;

import com.ryanmhub.fitnessapp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Todo: I need to change this Mapping or eliminate the entire controller
@RestController
@RequestMapping("/api/auth")
public class AppUserController {

    @Autowired
    private UserService userService;


}
