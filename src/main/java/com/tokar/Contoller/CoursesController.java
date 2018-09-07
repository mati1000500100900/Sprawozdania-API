package com.tokar.Contoller;

import com.tokar.DataPrototypes.CoursesPrototype;
import com.tokar.Entity.DefinedReports;
import com.tokar.DataPrototypes.Message;
import com.tokar.Entity.Users;
import com.tokar.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.tokar.Entity.Courses;
import com.tokar.Repository.CoursesRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/courses")
public class CoursesController {
    private final CoursesRepository coursesRepo;
    private final UsersRepository usersRepo;

    @Autowired
    public CoursesController(CoursesRepository coursesRepo, UsersRepository usersRepo) {
        this.coursesRepo = coursesRepo;
        this.usersRepo = usersRepo;
    }

    @GetMapping
    public Iterable<Courses> GetAll(){
    return coursesRepo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Courses> getCourse(@PathVariable("id") Long id){
    return coursesRepo.findById(id);
    }

    @DeleteMapping("{id}")
    public Message DeleteCourse(HttpServletRequest request,@PathVariable("id") Long id) throws ServletException{
        Optional<Courses> opCourse = coursesRepo.findById(id);
        if(!opCourse.isPresent()){
            throw new ServletException("no such course");
        }
        Courses course = opCourse.get();
        if(!course.getMaster().getEmail().equals(request.getAttribute("subject").toString())){
            throw new ServletException("not yours");
        }
        coursesRepo.delete(course);
        return new Message("deleted");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Message insertCourse(HttpServletRequest request, @RequestBody CoursesPrototype newCourse ) throws ServletException{
        if(newCourse.getName()==null || newCourse.getStart_time()==null || newCourse.getEnd_time()==null){
            throw new  ServletException("empty parameters");
        }

        Users master = usersRepo.findByEmail(request.getAttribute("subject").toString());
        if(master == null){
            throw new ServletException("no such user");
        }

        if(!master.getRoles().contains("ROLE_TEACHER")){
            throw new ServletException("wrong permissions");
        }

        Courses course = new Courses(newCourse);
        course.setMaster(master);
        coursesRepo.save(course);

        return new Message("added");
    }
    @PatchMapping("/{id}")
    public Message updateCourse(HttpServletRequest request, @RequestBody CoursesPrototype newCourse, @PathVariable("id") Long id ) throws ServletException{
        Optional<Courses> oldCourse = coursesRepo.findById(id);
        if(!oldCourse.isPresent()){
            throw new ServletException("no such course");
        }
        Courses oldCourse2 = oldCourse.get();
        if(!oldCourse2.getMaster().getEmail().equals(request.getAttribute("subject").toString())){
            throw new ServletException("not yours");
        }
        oldCourse2.updateCourse(newCourse);
        coursesRepo.save(oldCourse2);
        return new Message("updated");
    }

    @GetMapping("/{id}/students")
    public Set<Users> getCourseUsers(@PathVariable("id") Long id) throws ServletException{
        Optional<Courses> opCourse = coursesRepo.findById(id);
        if(!opCourse.isPresent()){
            throw new ServletException("no such course");
        }
        Courses course = opCourse.get();
        return course.getStudents();

    }

    @GetMapping("/{id}/definitions")
    public List<DefinedReports> getCourseDefinitions(@PathVariable("id") Long id) throws ServletException{
        Optional<Courses> opCourse = coursesRepo.findById(id);
        if(!opCourse.isPresent()){
            throw new ServletException("no such course");
        }
        Courses course = opCourse.get();
        return course.getDefinedReports();

    }

    @GetMapping("/my")
    public Iterable<Courses> getMyCourses(HttpServletRequest request){
        Users user = usersRepo.findByEmail(request.getAttribute("subject").toString());
        return user.getCoursesSet();
    }

    @PostMapping("/join/{access_key}")
    public Message joinCourse(HttpServletRequest request, @PathVariable("access_key") String access_key) throws ServletException{
        Optional<Courses> opCourse = coursesRepo.findByAccesskey(access_key);
        if(!opCourse.isPresent()){
            throw new ServletException("no such course");
        }
        Users user = usersRepo.findByEmail(request.getAttribute("subject").toString());
        if(!user.getRoles().contains("ROLE_STUDENT")){
            throw new ServletException("only for students");
        }
        Courses course = opCourse.get();
        course.addStudentToSet(user);
        coursesRepo.save(course);
        return new Message("joined");
    }
}
