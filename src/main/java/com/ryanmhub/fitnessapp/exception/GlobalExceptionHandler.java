package com.ryanmhub.fitnessapp.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ryanmhub.fitnessapp.common.response.ApiResponse;
import com.ryanmhub.fitnessapp.auth.response.AuthenticationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.AuthenticationException;


@ControllerAdvice
public class GlobalExceptionHandler {

    //Generates a response with message and status based on the derivative of the AuthenticationException
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex){
        String responseMessage;
        HttpStatus status;

        if(ex instanceof BadCredentialsException){
            responseMessage = "Invalid username or password";
            status = HttpStatus.UNAUTHORIZED;
        } else if(ex instanceof UsernameNotFoundException){
            responseMessage = "User not found";
            status = HttpStatus.NOT_FOUND;
        } else if(ex instanceof LockedException){
            responseMessage = "Account is locked";
            status = HttpStatus.LOCKED;
        } else if(ex instanceof DisabledException){
            responseMessage = "Account is disabled";
            status = HttpStatus.FORBIDDEN;
        } else if(ex instanceof AccountExpiredException){
            responseMessage = "Account expired";
            status = HttpStatus.GONE;
        } else if(ex instanceof CredentialsExpiredException){
            responseMessage = "Credentials Expired";
            status = HttpStatus.GONE;
        } else {
            responseMessage = "Authentication Failed";
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(new AuthenticationResponse.Builder().message(responseMessage).success(false).build(), status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex){
        return new ResponseEntity<>(ApiResponse.builder().success(false).message("An error occurred: " + ex.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ApiResponse> handleJsonProcessingException(JsonProcessingException ex){
        return new ResponseEntity<>(ApiResponse.builder().success(false).message("An error occurred: " + ex.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
