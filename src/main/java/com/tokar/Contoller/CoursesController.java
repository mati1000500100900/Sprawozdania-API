package com.tokar.Contoller;

import com.tokar.DataPrototypes.CoursesPrototype;
import com.tokar.DataPrototypes.DefinedReportsPrototype;
import com.tokar.Entity.DefinedReports;
import com.tokar.DataPrototypes.Message;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/courses")
public class CoursesController {
    @Autowired
    private CoursesRepository coursesRepo;
    @Autowired
    private UsersRepository usersRepo;
    @Autowired
    private DefinedReportsRepository definedReportsRepo;


    @GetMapping
    public Iterable<Courses> GetAll(){
    return coursesRepo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Courses> getCourse(@PathVariable("id") int id){
    return coursesRepo.findById(Long.valueOf(id));
    }

    @DeleteMapping("{id}")
    public Message DeleteCourse(HttpServletRequest request,@PathVariable("id") Long id) throws ServletException{
        Optional<Courses> opCourse = coursesRepo.findById(id);
        if(!opCourse.isPresent()){
            throw new ServletException("no such course");
        }
        Courses course = opCourse.get();
        if(!course.getMaster().getEmail().equals(request.getAttribute("subject").toString())){
            throw new ServletException("cant delete not your course");
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

        return new Message("new course added");
    }
    @PatchMapping("/{id}")
    public Message updateCourse(HttpServletRequest request, @RequestBody CoursesPrototype newCourse, @PathVariable("id") Long id ) throws ServletException{
        Optional<Courses> oldCourse = coursesRepo.findById(id);
        if(!oldCourse.isPresent()){
            throw new ServletException("no such course");
        }
        Courses oldCourse2 = oldCourse.get();
        if(!oldCourse2.getMaster().getEmail().equals(request.getAttribute("subject").toString())){
            throw new ServletException("cannot change not your course");
        }
        oldCourse2.updateCourse(newCourse);
        coursesRepo.save(oldCourse2);
        return new Message("updated");
    }

    @GetMapping("/{id}/students")
    public Set<Users> getCourseUsers(@PathVariable("id") Long id) throws ServletException{
        Optional<Courses> opCourse = coursesRepo.findById(id);
        if(!opCourse.isPresent()){
            throw new ServletException("No such course");
        }
        Courses course = opCourse.get();
        return course.getStudents();

    }

    @GetMapping("/{id}/definitions")
    public List<DefinedReports> getCourseDefinitions(@PathVariable("id") Long id) throws ServletException{
        Optional<Courses> opCourse = coursesRepo.findById(id);
        if(!opCourse.isPresent()){
            throw new ServletException("No such course");
        }
        Courses course = opCourse.get();
        return course.getDefinedReports();

    }

    @GetMapping("/my")
    public Iterable<Courses> getMyCourses(HttpServletRequest request) throws ServletException{
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
            throw new ServletException("only students can be signed to course");
        }
        Courses course = opCourse.get();
        course.addStudentToSet(user);
        coursesRepo.save(course);
        return new Message("joined");
    }

    @GetMapping("/definitions")
    public Iterable<DefinedReports> getDefinedReports(){
        return definedReportsRepo.findAll();
    }

    @PostMapping(value = "/definitions", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Message addDefinedReports(HttpServletRequest request, @RequestBody DefinedReportsPrototype newDefinition) throws ServletException{
        if(newDefinition.getTitle()==null || newDefinition.getStart_time()==null || newDefinition.getEnd_time()==null || newDefinition.getCourse_id()==null){
            throw new ServletException("empty parameters");
        }
        Optional<Courses> opcourse = coursesRepo.findById(newDefinition.getCourse_id());
        if(!opcourse.isPresent()){
            throw new ServletException("no such course");
        }
        Courses course = opcourse.get();
        if(!course.getMaster().getEmail().equals(request.getAttribute("subject").toString())){
            throw new ServletException("cant add new definition to not your course");
        }

        DefinedReports definedReport = new DefinedReports(newDefinition);
        definedReport.setCourse(course);

        definedReportsRepo.save(definedReport);
        return new Message("definition added");
    }

    @GetMapping("/definitions/{id}")
    public Optional<DefinedReports> getOneDefinition(@PathVariable("id") Long id){
        return definedReportsRepo.findById(id);
    }

    @PatchMapping("/definitions/{id}")
    public Message updateDefinition (HttpServletRequest request, @RequestBody DefinedReportsPrototype newDefinition, @PathVariable("id") Long id) throws ServletException{
        Optional<DefinedReports> opDefinition = definedReportsRepo.findById(id);
        if(!opDefinition.isPresent()){
            throw new ServletException("no such definition");
        }
        DefinedReports definition = opDefinition.get();
        if(!definition.getCourse().getMaster().getEmail().equals(request.getAttribute("subject").toString())){
            throw new ServletException("cant update not your definition");
        }
        definition.updateDefinition(newDefinition);
        definedReportsRepo.save(definition);
        return new Message("updated");
    }

    @DeleteMapping("/definitions/{id}")
    public Message DeleteDefinition(HttpServletRequest request,@PathVariable("id") Long id) throws ServletException{
        Optional<DefinedReports> opDefinition = definedReportsRepo.findById(id);
        if(!opDefinition.isPresent()){
            throw new ServletException("no such definition");
        }
        DefinedReports definition = opDefinition.get();
        if(!definition.getCourse().getMaster().getEmail().equals(request.getAttribute("subject").toString())){
            throw new ServletException("cant delete not your definition");
        }
        definedReportsRepo.delete(definition);
        return new Message("deleted");
    }

}
