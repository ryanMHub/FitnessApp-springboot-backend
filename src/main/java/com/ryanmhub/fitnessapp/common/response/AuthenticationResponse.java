package com.ryanmhub.fitnessapp.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse extends ApiResponse{

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refreshToken")
    private String refreshToken;

    protected AuthenticationResponse(Builder builder){
        super(builder);
        this.accessToken = builder.accessToken;
        this.refreshToken = builder.refreshToken;
    }


    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends ApiResponse.Builder<Builder>{
        private String accessToken = "";
        private String refreshToken = "";

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public AuthenticationResponse build(){
            return new AuthenticationResponse(this);
        }
    }
}
