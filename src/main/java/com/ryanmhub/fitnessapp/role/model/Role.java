package com.ryanmhub.fitnessapp.role.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    //Links Role to UserRole by role_id
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRole;

    public Role() {
    }

    public Role(Builder builder) {
        this.name = builder.name;
    }

    public static class Builder {
        private String name;

        public Builder(){}

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Role build(){
            return new Role(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    @Override
//    public String toString() {
//        return "Role{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", userRole=" + userRole +
//                '}';
//    }
}
