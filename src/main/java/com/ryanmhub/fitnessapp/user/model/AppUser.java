package com.ryanmhub.fitnessapp.user.model;

import com.ryanmhub.fitnessapp.user.repository.AppUserRoleRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


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

    //constructors
    public AppUser() {
    }

    public AppUser(String firstName, String lastName, String username, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.notExpired = true;
        this.notLocked = true;
        this.isEnabled = true;
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

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Transient
    private AppUserRoleRepository userRoleRepository;

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(userRoleRepository != null){
            List<AppUserRole> roles = userRoleRepository.findByUsername(this.username);
            return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
        }
        return List.of();
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

    public void setNotExpired(boolean notExpired) {
        this.notExpired = notExpired;
    }

    public void setNotLocked(boolean notLocked) {
        this.notLocked = notLocked;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
