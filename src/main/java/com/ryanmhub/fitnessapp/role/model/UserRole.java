package com.ryanmhub.fitnessapp.role.model;

import com.ryanmhub.fitnessapp.user.model.AppUser;
import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    public UserRole(){

    }

    public UserRole(Builder builder){
        this.user = builder.user;
        this.role = builder.role;
    }

    public static class Builder {
        private AppUser user;
        private Role role;

        public Builder user(AppUser user){
            this.user = user;
            return this;
        }

        public Builder role(Role role){
            this.role = role;
            return this;
        }

        public UserRole build(){
            return new UserRole(this);
        }
    }

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public Role getRole() {
        return role;
    }

//    @Override
//    public String toString() {
//        return "UserRole{" +
//                "id=" + id +
//                ", user=" + user +
//                ", role=" + role +
//                '}';
//    }
}
