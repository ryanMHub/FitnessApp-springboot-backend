package com.ryanmhub.fitnessapp.token;

import com.ryanmhub.fitnessapp.user.model.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public Long id;

    @NotBlank
    @Column(name="token", nullable = false, unique=true)
    public String token;

    @NotBlank
    @Column(name = "token_type")
    public String tokenType;

    @Column(nullable = false)
    public boolean revoked;

    @Column(nullable = false)
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public AppUser user;

    public Token() {}

    public Token(Builder builder) {
        this.token = builder.token;
        this.tokenType = builder.tokenType.toString();
        this.revoked = builder.revoked;
        this.expired = builder.expired;
        this.user = builder.user;
    }

    public static class Builder {
        private Integer id;
        private String token;
        private TokenType tokenType;
        private boolean revoked = false;
        private boolean expired = false;
        private AppUser user;

        public Builder(){

        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder tokenType(TokenType tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public Builder revoked(boolean revoked) {
            this.revoked = revoked;
            return this;
        }

        public Builder expired(boolean expired) {
            this.expired = expired;
            return this;
        }

        public Builder user(AppUser user) {
            this.user = user;
            return this;
        }

        public Token build(){
            return new Token(this);
        }
    }

    //Getters
    public Long getId() {
        return id;
    }

    public @NotBlank String getToken() {
        return token;
    }

    public @NotBlank String getTokenType() {
        return tokenType;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public boolean isExpired() {
        return expired;
    }

    public AppUser getUser() {
        return user;
    }


    //Setters
    public void setToken(@NotBlank String token) {
        this.token = token;
    }

    public void setTokenType(@NotBlank TokenType tokenType) {
        this.tokenType = tokenType.toString();
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }
}
