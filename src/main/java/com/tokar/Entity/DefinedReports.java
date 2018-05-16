package com.tokar.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tokar.DataPrototypes.DefinedReportsPrototype;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity(name = "defined_reports")
public class DefinedReports {
    @Transient
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    @JsonBackReference
    private Courses course;

    public DefinedReports(){}

    public DefinedReports(DefinedReportsPrototype proto){
        this.title=proto.getTitle();
        this.setStart_time(proto.getStart_time());
        this.setEnd_time(proto.getEnd_time());
    }

    public void updateDefinition(DefinedReportsPrototype proto){
        if(proto.getTitle()!=null) this.title=proto.getTitle();
        if(proto.getStart_time()!=null) this.setStart_time(proto.getStart_time());
        if(proto.getEnd_time()!=null) this.setEnd_time(proto.getEnd_time());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = LocalDateTime.parse(start_time, this.formatter);
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = LocalDateTime.parse(end_time, this.formatter);
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }
}
