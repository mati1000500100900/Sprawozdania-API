package com.tokar.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tokar.DataPrototypes.UsersPrototype;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Users {

    @Transient //STUDENT: 1, TEACHER: 2, ADMIN: 4
    private String[] rolesArray = {"ROLE_STUDENT","ROLE_TEACHER","ROLE_ADMIN"};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String last_name;
    private String password;
    private int roles;
    @OneToMany(mappedBy = "master")
    @JsonBackReference
    private List<Courses> courses;
    @ManyToMany(mappedBy = "students")
    @JsonBackReference
    private Set<Courses> coursesSet;


    public Users(){}

    public Users(UsersPrototype proto){
        this.email=proto.getEmail();
        this.name=proto.getName();
        this.last_name=proto.getLast_name();
        this.setPassword(proto.getPassword());
    }

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

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        this.password = encoder.encode(password);
    }

    public boolean checkPassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, this.password);
    }

    public ArrayList<String> getRoles() {
        ArrayList<String> userRoles = new ArrayList<>();
        for(int i=0; i<this.rolesArray.length; i++){
            if((this.roles >> i)%2==1) userRoles.add(rolesArray[i]);
        }
        return userRoles;
    }

    public void setRoles(int roles) {
        this.roles = roles;
    }

    public Set<Courses> getCoursesSet() {
        return coursesSet;
    }

    public void setCoursesSet(Set<Courses> coursesSet) {
        this.coursesSet = coursesSet;
    }

    public void addCourseToSet(Courses course){
        this.courses.add(course);
    }

    public void deleteCourseFromSet(Courses course){
        this.courses.remove(course);
    }
}
