package com.ryanmhub.fitnessapp.common.response;

//Todo: Probably not needed
public class ApiResponse {

    private boolean success;
    private String message;

    public ApiResponse(){

    }

    public ApiResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess(){
        return success;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
