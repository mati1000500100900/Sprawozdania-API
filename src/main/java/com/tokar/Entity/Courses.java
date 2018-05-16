package com.tokar.Entity;

import javax.persistence.*;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tokar.DataPrototypes.CoursesPrototype;

@Entity
public class Courses
{
    @Transient
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "master_id")
    @JsonManagedReference
    private Users master;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    @Column(name = "access_key")
    private String accesskey;
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DefinedReports> definedReports;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "course_users", joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @JsonBackReference
    private Set<Users> students;

    public Courses(){}

    public Courses(CoursesPrototype proto){
        this.name=proto.getName();
        this.setStart_time(proto.getStart_time());
        this.setEnd_time(proto.getEnd_time());
        this.setAccess_key();
    }
    @Transient
    public void updateCourse(CoursesPrototype proto){
        if(proto.getName()!=null) this.setName(proto.getName());
        if(proto.getStart_time()!=null) this.setStart_time(proto.getStart_time());
        if(proto.getEnd_time()!=null) this.setEnd_time(proto.getEnd_time());
    }

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
        return accesskey;
    }

    public void setAccess_key() {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( 16 );
        for( int i = 0; i < 16; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        this.accesskey = sb.toString();
    }

    public List<DefinedReports> getDefinedReports() {
        return definedReports;
    }

    public void setDefinedReports(List<DefinedReports> definedReports) {
        this.definedReports = definedReports;
    }

    public Set<Users> getStudents() {
        return students;
    }

    public void setStudents(Set<Users> students) {
        this.students = students;
    }

    public void addStudentToSet(Users student){
        this.students.add(student);
    }

    public void deleteStudentFromSet(Users student){
        this.students.remove(student);
    }

}
