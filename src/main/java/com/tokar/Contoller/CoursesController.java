package com.tokar.Contoller;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void insertCourse(
            @RequestParam("name") String name,
            @RequestParam("master_id") int master_id,
            @RequestParam("start_time") String start_time,
            @RequestParam("end_time") String end_time
    ) throws ServletException{
        if(name.isEmpty() || start_time.isEmpty() || end_time.isEmpty()){
            throw new  ServletException("Empty parameters!");
        }
        Optional<Users> masterop = uRepo.findById(Long.valueOf(master_id));
        if(masterop == null){
            throw new ServletException("No such user");
        }
        Users master = masterop.get();

        if(!master.getRoles().contains("ROLE_TEACHER")){
            throw new ServletException("Wrong permissions");
        }

        Courses course = new Courses();
        course.setName(name);
        course.setStart_time(start_time);
        course.setEnd_time(end_time);
        course.setAccess_key("jp2gmd2137");
        course.setMaster(master);

        CR.save(course);
    }

}
