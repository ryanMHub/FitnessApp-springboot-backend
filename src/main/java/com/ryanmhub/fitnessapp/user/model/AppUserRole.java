package com.ryanmhub.fitnessapp.user.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
public class AppUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "role", nullable = false)
    private String role;

    public AppUserRole() {
    }

    public AppUserRole(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
