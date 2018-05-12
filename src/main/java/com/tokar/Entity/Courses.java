package com.tokar.Entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tokar.Entity.Users;

@Entity
public class Courses
{
    @Transient
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    //private Long master_id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "master_id")
    @JsonManagedReference
    private Users master;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private String access_key;

    public Users getMaster() {
        return master;
    }

    public void setMaster(Users master) {
        this.master = master;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public Long getMaster_id() {
        return master_id;
    }

    public void setMaster_id(Long master_id) {
        this.master_id = master_id;
    }*/

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = LocalDateTime.parse(start_time, this.formatter);
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String  end_time) {
        this.end_time = LocalDateTime.parse(end_time, this.formatter);
    }

    public String getAccess_key() {
        return access_key;
    }

    public void setAccess_key(String acces_key) {
        this.access_key = acces_key;
    }
}
