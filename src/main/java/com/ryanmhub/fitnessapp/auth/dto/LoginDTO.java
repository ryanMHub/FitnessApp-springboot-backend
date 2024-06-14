package com.ryanmhub.fitnessapp.auth.dto;

import jakarta.validation.constraints.NotBlank;

//Data Transfer Object primarily for authentication purposes
public class LoginDTO {

    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;

    public LoginDTO() {
    }

    public LoginDTO(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
