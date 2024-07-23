package com.ryanmhub.fitnessapp.auth.dto;


//Todo: How are we going to transfer the required information. I'm not sure if this is needed
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
