package com.ryanmhub.fitnessapp.token;

public enum TokenType {
    BEARER("Bearer ");

    private final String prefix;

    TokenType(String prefix){
        this.prefix = prefix;
    }

    public String getPrefix(){
        return prefix;
    }
}
