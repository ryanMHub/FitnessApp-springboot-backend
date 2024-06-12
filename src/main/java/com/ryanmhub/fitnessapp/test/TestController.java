package com.ryanmhub.fitnessapp.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/print")
    private void displaySomething(){
        System.out.println("Alright");
    }
}
