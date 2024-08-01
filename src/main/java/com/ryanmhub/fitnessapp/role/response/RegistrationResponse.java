package com.ryanmhub.fitnessapp.role.response;

import com.ryanmhub.fitnessapp.common.response.ApiResponse;

public class RegistrationResponse extends ApiResponse {
    //Todo: What additional uses do I have for this instead of using simple ApiResponse
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
