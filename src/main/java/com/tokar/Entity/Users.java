package com.tokar.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Users {

    @Transient //STUDENT: 1, TEACHER: 2, ADMIN: 4
    String[] rolesArray = {"ROLE_STUDENT","ROLE_TEACHER","ROLE_ADMIN"};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String last_name;
    @JsonIgnore
    private String password;
    private int roles;
    @OneToMany(mappedBy = "master")
    //@JsonBackReference
    //private List<Courses> courses;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        this.password = encoder.encode(password);
    }

    public boolean checkPassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, this.password);
    }

    public ArrayList<String> getRoles() {
        ArrayList<String> userRoles = new ArrayList<String>();
        for(int i=0; i<this.rolesArray.length; i++){
            if((this.roles >> i)%2==1) userRoles.add(rolesArray[i]);
        }
        return userRoles;
    }

    public void setRoles(int roles) {
        this.roles = roles;
    }
}