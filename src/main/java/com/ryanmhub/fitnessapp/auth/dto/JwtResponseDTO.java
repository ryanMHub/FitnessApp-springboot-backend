package com.ryanmhub.fitnessapp.auth.dto;

public class JwtResponseDTO {

    private String token;

    public JwtResponseDTO(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }
}
