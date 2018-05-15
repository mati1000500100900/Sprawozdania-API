package com.tokar.Contoller;

import com.tokar.DataPrototypes.CoursesPrototype;
import com.tokar.Entity.DefinedReports;
import com.tokar.Entity.Message;
import com.tokar.Entity.Users;
import com.tokar.Repository.DefinedReportsRepository;
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
    private CoursesRepository coursesRepository;
    @Autowired
    private UsersRepository uRepo;
    @Autowired
    private DefinedReportsRepository definedReportsRepository;


    @GetMapping("/get")
    public String getUser(HttpServletRequest request){
         if (request.getAttribute("roles").toString().contains("ROLE_STUDENT")){
             return "student";
         }
         return "nie";
    }

    @GetMapping
    public Iterable<Courses> GetAll(){
    return coursesRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Courses> getCourse(@PathVariable("id") int id){
    return coursesRepository.findById(Long.valueOf(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Message insertCourse(HttpServletRequest request, @RequestBody CoursesPrototype newCourse ) throws ServletException{
        if(newCourse.getName()==null || newCourse.getStart_time()==null || newCourse.getEnd_time()==null){
            throw new  ServletException("empty parameters");
        }

        Users master = uRepo.findByEmail(request.getAttribute("subject").toString());
        if(master == null){
            throw new ServletException("no such user");
        }

        if(!master.getRoles().contains("ROLE_TEACHER")){
            throw new ServletException("wrong permissions");
        }

        Courses course = new Courses();
        course.setName(newCourse.getName());
        course.setStart_time(newCourse.getStart_time());
        course.setEnd_time(newCourse.getEnd_time());
        course.setAccess_key();
        course.setMaster(master);
        coursesRepository.save(course);

        return new Message("new course added");
    }
    @PatchMapping("/{id}")
    public Message updateCourse(HttpServletRequest request, @RequestBody CoursesPrototype newCourse, @PathVariable("id") Long id ) throws ServletException{
        Optional<Courses> oldCourse = coursesRepository.findById(id);
        if(!oldCourse.isPresent()){
            throw new ServletException("no such course");
        }
        Courses oldCourse2 = oldCourse.get();
        if(!oldCourse2.getMaster().getEmail().equals(request.getAttribute("subject").toString())){
            throw new ServletException("cannot change not your course");
        }
        if(newCourse.getName()!=null) oldCourse2.setName(newCourse.getName());
        if(newCourse.getStart_time()!=null) oldCourse2.setStart_time(newCourse.getStart_time());
        if(newCourse.getEnd_time()!=null) oldCourse2.setEnd_time(newCourse.getEnd_time());
        coursesRepository.save(oldCourse2);
        return new Message("updated");
    }

    @GetMapping("/definitions")
    public Iterable<DefinedReports> getDefinedReports(){
        return definedReportsRepository.findAll();
    }

    @PostMapping(value = "/definitions", consumes = MediaType.APPLICATION_JSON_VALUE)
    private DefinedReports addDefinedReports(HttpServletRequest request, @RequestBody DefinedReports newDefinition) throws ServletException{
        definedReportsRepository.save(newDefinition);
        return newDefinition;
    }

}
