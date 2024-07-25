package com.ryanmhub.fitnessapp.user.model;

import com.ryanmhub.fitnessapp.role.model.UserRole;
import com.ryanmhub.fitnessapp.role.repository.UserRoleRepository;
import com.ryanmhub.fitnessapp.token.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//Todo: Add role into the Builder
//The User Entity will contain all of the authorization and registration of the user. The profile will be handled in a separate table.
@Entity
@Table(name = "users")
public class AppUser implements UserDetails {

    //currently the id number will be generated when each new instance is created may change key later
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank
    @Size(max = 40)
    @Column(name="first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Size(max = 40)
    @Column(name="last_name", nullable = false)
    private String lastName;

    @NotBlank
    @Size(max = 15)
    @Column(name="username", nullable = false, unique = true)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Email
    @Column(name="email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(name="password", nullable = false)
    private String password;

    @Column(name="not_expired", nullable = false)
    private boolean notExpired;

    @Column(name="not_locked", nullable = false)
    private boolean notLocked;

    @Column(name="enabled", nullable = false)
    private boolean isEnabled;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> roles;

    //constructors
    public AppUser() {
    }

    public AppUser(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
        this.notExpired = true;
        this.notLocked = true;
        this.isEnabled = true;
    }

    public static class Builder{
        private String firstName;
        private String lastName;
        private String username;
        private String email;
        private String password;

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
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

        public AppUser build(){
            return new AppUser(this);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(roles != null){
            return roles.stream().map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName())).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    //Used to return the roles in string value to store in the JWT
    @Transactional(readOnly = true)
    public List<String> getRolesNames(){
        if(roles != null){
            return roles.stream()
                    .map(role -> role.getRole().getName())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    //Getters
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

    @Override
    public boolean isAccountNonExpired() {
        return notExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return notLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } //Todo: This is not handled or stored anywhere

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    //Todo: Should I remove this
    @Transactional(readOnly = true)
    public List<UserRole> getRoles() {
        return roles;
    }

    //Setters

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

    public void setNotExpired(boolean notExpired) {
        this.notExpired = notExpired;
    }

    public void setNotLocked(boolean notLocked) {
        this.notLocked = notLocked;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", username='" + username + '\'' +
//                ", email='" + email + '\'' +
//                ", password='" + password + '\'' +
//                '}';
//    }
}
