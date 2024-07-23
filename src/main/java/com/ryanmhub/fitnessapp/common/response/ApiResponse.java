package com.ryanmhub.fitnessapp.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.authentication.AuthenticationProvider;

//Todo: Probably not needed
public class ApiResponse {

    @JsonProperty("success")
    private boolean success;
    @JsonProperty("message")
    private String message;

    protected ApiResponse(Builder<?> builder){
        this.success = builder.success;
        this.message = builder.message;
    }

    public static Builder<?> builder(){
        return new Builder<>();
    }

    public static class Builder<T extends Builder<T>> {
        private boolean success = false;
        private String message = "";

        @SuppressWarnings("unchecked")
        public T success(boolean success) {
            this.success = success;
            return (T)this;
        }

        @SuppressWarnings("unchecked")
        public T message(String message) {
            this.message = message;
            return (T)this;
        }

        public ApiResponse build(){
            return new ApiResponse(this);
        }
    }
}
