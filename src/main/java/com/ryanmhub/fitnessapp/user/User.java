package com.ryanmhub.fitnessapp.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


//The User Entity will contain all of the authorization and registration of the user. The profile will be handled in a separate table.
@Entity
@Table(name = "users")
public class User {

    //currently the id number will be generated when each new instance is created may change key later
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank
    @Size(max = 40)
    @Column(name="first_name")
    private String firstName;

    @NotBlank
    @Size(max = 40)
    @Column(name="last_name")
    private String lastName;

    @NotBlank
    @Size(max = 15)
    @Column(name="username")
    private String username;

    @NotBlank
    @Size(max = 100)
    @Email
    @Column(name="email")
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(name="password")
    private String password;

    //constructors
    public User() {
    }

    public User(String firstName, String lastName, String username, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //getters and setters *I'm going to leave out the setter for Id right now
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
