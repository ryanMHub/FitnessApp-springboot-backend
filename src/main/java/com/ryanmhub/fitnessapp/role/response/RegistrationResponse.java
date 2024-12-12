package com.ryanmhub.fitnessapp.role.response;

import com.ryanmhub.fitnessapp.common.response.ApiResponse;

//Custom response for registration
public class RegistrationResponse extends ApiResponse {

    protected RegistrationResponse(Builder builder){
        super(builder);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends ApiResponse.Builder<Builder>{
        public RegistrationResponse build(){
            return new RegistrationResponse(this);
        }
    }
}
