package com.ryanmhub.fitnessapp.test.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ryanmhub.fitnessapp.common.response.ApiResponse;

//used to return user data to client
public class UserDataResponse extends ApiResponse {

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    protected UserDataResponse(Builder builder) {
        super(builder);
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
    }

    //Todo: make sure that all Builder files have this modification
    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends ApiResponse.Builder<Builder> {

        private String firstName = "";
        private String lastName = "";
        private String username = "";
        private String email = "";
        private String password = "";

        public Builder firstName(String firstName){
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName){
            this.lastName = lastName;
            return this;
        }

        public Builder username(String username){
            this.username = username;
            return this;
        }

        public Builder email(String email){
            this.email = email;
            return this;
        }

        public Builder password(String password){
            this.password = password;
            return this;
        }

        public UserDataResponse build() {
            return new UserDataResponse(this);
        }
    }
}
