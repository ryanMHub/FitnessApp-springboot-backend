package com.ryanmhub.fitnessapp.config.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ryanmhub.fitnessapp.common.response.ApiResponse;

public class LogoutResponse extends ApiResponse {

    @JsonProperty("logout_info")
    private String logoutInfo;

    protected LogoutResponse(Builder builder) {
        super(builder);
        this.logoutInfo = builder.logout_info;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends ApiResponse.Builder<Builder> {
        private String logout_info = "User Logged Out";

        public Builder logout_info(String logout_info) {
            this.logout_info = logout_info;
            return this;
        }

        public LogoutResponse build() {
            return new LogoutResponse(this);
        }
    }
}
