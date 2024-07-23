package com.ryanmhub.fitnessapp.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refreshToken")
    private String refreshToken;
    @JsonProperty("message")
    private String message;

    private AuthenticationResponse(Builder builder){
        this.accessToken = builder.accessToken;
        this.refreshToken = builder.refreshToken;
        this.message = builder.message;
    }

    public static class Builder {
        private String accessToken = "";
        private String refreshToken = "";
        private String message = "";

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public AuthenticationResponse build(){
            return new AuthenticationResponse(this);
        }
    }
}
