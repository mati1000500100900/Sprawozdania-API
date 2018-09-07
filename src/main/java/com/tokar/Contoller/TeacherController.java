package com.tokar.Contoller;


import com.tokar.DataPrototypes.Message;
import com.tokar.Entity.Courses;
import com.tokar.Entity.Users;
import com.tokar.Repository.CoursesRepository;
import com.tokar.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    final
    UsersRepository usersRepo;
    final
    CoursesRepository coursesRepo;

    @Autowired
    public TeacherController(UsersRepository usersRepo, CoursesRepository coursesRepo) {
        this.usersRepo = usersRepo;
        this.coursesRepo = coursesRepo;
    }

    @GetMapping("/courses")
    public Iterable<Courses> GetAll(HttpServletRequest request) throws ServletException{
        Users master = usersRepo.findByEmail(request.getAttribute("subject").toString());
        if(master == null){
            throw new ServletException("no such user");
        }
        if(!master.getRoles().contains("ROLE_TEACHER")){
            throw new ServletException("wrong permissions");
        }

        return coursesRepo.findAllByMaster(master);
    }

    @GetMapping("/mystudents")
    public Iterable<Users> GetMyStudents(HttpServletRequest request) throws ServletException{
        Users master = usersRepo.findByEmail(request.getAttribute("subject").toString());
        if(master == null){
            throw new ServletException("no such user");
        }
        if(!master.getRoles().contains("ROLE_TEACHER")){
            throw new ServletException("wrong permissions");
        }
        ArrayList<Users> myStudents = new ArrayList<>();

        Iterable<Courses> myCourses = coursesRepo.findAllByMaster(master);
        for(Courses course:myCourses){
            myStudents.addAll(course.getStudents());
        }
        return myStudents;
    }

    @GetMapping("/kickstudent/{courseid}/{studentid}")
    public Message kickStudent(HttpServletRequest request, @PathVariable("courseid") Long courseid, @PathVariable("studentid") Long studentid) throws ServletException{
        Optional<Courses> course = coursesRepo.findById(courseid);
        if(!course.isPresent()){
            throw new ServletException("no such course");
        }
        Courses actualCourse=course.get();

        Optional<Users> student = usersRepo.findById(studentid);
        if(!student.isPresent()){
            throw new ServletException("no such student");
        }
        Users actualStudent = student.get();

        if(!actualCourse.getStudents().contains(actualStudent)){
            throw new ServletException("student doesnt singed");
        }

        Users master = usersRepo.findByEmail(request.getAttribute("subject").toString());
        if(master == null){
            throw new ServletException("no such user");
        }
        if(!master.getRoles().contains("ROLE_TEACHER")){
            throw new ServletException("wrong permissions");
        }
        if(!actualCourse.getMaster().equals(master)){
            throw new ServletException("not your course");
        }

        Set<Users> students = actualCourse.getStudents();
        students.remove(actualStudent);
        actualCourse.setStudents(students);
        coursesRepo.save(actualCourse);

        return new Message("kicked");
    }


}
