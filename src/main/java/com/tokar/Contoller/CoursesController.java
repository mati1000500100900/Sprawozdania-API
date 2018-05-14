package com.tokar.Contoller;

import com.tokar.DataPrototypes.CoursesPrototype;
import com.tokar.Entity.Message;
import com.tokar.Entity.Users;
import com.tokar.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.tokar.Entity.Courses;
import com.tokar.Repository.CoursesRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CoursesController {
    @Autowired
    private CoursesRepository CR;
    @Autowired
    private UsersRepository uRepo;

    @GetMapping("/get")
    public String getUser(HttpServletRequest request){
         if (request.getAttribute("roles").toString().contains("ROLE_STUDENT")){
             return "student";
         }
         return "nie";
    }

    @GetMapping
    public Iterable<Courses> GetAll(){
    return CR.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Courses> getCourse(@PathVariable("id") int id){
    return CR.findById(Long.valueOf(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Message insertCourse(HttpServletRequest request, @RequestBody CoursesPrototype newCourse ) throws ServletException{
        if(newCourse.getName()==null || newCourse.getStart_time()==null || newCourse.getEnd_time()==null){
            throw new  ServletException("Empty parameters!");
        }

        Users master = uRepo.findByEmail(request.getAttribute("subject").toString());
        if(master == null){
            throw new ServletException("No such user");
        }

        if(!master.getRoles().contains("ROLE_TEACHER")){
            throw new ServletException("Wrong permissions");
        }

        Courses course = new Courses();
        course.setName(newCourse.getName());
        course.setStart_time(newCourse.getStart_time());
        course.setEnd_time(newCourse.getEnd_time());
        course.setAccess_key();
        course.setMaster(master);
        CR.save(course);

        return new Message("New course added");
    }

}
