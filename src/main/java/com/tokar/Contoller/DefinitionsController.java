package com.tokar.Contoller;

import com.tokar.DataPrototypes.DefinedReportsPrototype;
import com.tokar.DataPrototypes.Message;
import com.tokar.Entity.Courses;
import com.tokar.Entity.DefinedReports;
import com.tokar.Entity.Users;
import com.tokar.Repository.CoursesRepository;
import com.tokar.Repository.DefinedReportsRepository;
import com.tokar.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/courses/definitions")
public class DefinitionsController {
    private final CoursesRepository coursesRepo;
    private final DefinedReportsRepository definedReportsRepo;
    private final UsersRepository usersRepo;

    @Autowired
    public DefinitionsController(CoursesRepository coursesRepo, DefinedReportsRepository definedReportsRepo, UsersRepository usersRepo) {
        this.coursesRepo = coursesRepo;
        this.definedReportsRepo = definedReportsRepo;
        this.usersRepo = usersRepo;
    }

    @GetMapping("/")
    public Iterable<DefinedReports> getDefinedReports(){
        return definedReportsRepo.findAll();
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Message addDefinedReports(HttpServletRequest request, @RequestBody DefinedReportsPrototype newDefinition) throws ServletException {
        if(newDefinition.getTitle()==null || newDefinition.getStart_time()==null || newDefinition.getEnd_time()==null || newDefinition.getCourse_id()==null){
            throw new ServletException("empty parameters");
        }
        Optional<Courses> opcourse = coursesRepo.findById(newDefinition.getCourse_id());
        if(!opcourse.isPresent()){
            throw new ServletException("no such course");
        }
        Courses course = opcourse.get();
        if(!course.getMaster().getEmail().equals(request.getAttribute("subject").toString())){
            throw new ServletException("not yours");
        }

        DefinedReports definedReport = new DefinedReports(newDefinition);
        definedReport.setCourse(course);

        definedReportsRepo.save(definedReport);
        return new Message("added");
    }

    @GetMapping("/{id}")
    public Optional<DefinedReports> getOneDefinition(@PathVariable("id") Long id){
        return definedReportsRepo.findById(id);
    }

    @PatchMapping("/{id}")
    public Message updateDefinition (HttpServletRequest request, @RequestBody DefinedReportsPrototype newDefinition, @PathVariable("id") Long id) throws ServletException{
        Optional<DefinedReports> opDefinition = definedReportsRepo.findById(id);
        if(!opDefinition.isPresent()){
            throw new ServletException("no such definition");
        }
        DefinedReports definition = opDefinition.get();
        if(!definition.getCourse().getMaster().getEmail().equals(request.getAttribute("subject").toString())){
            throw new ServletException("not yours");
        }
        definition.updateDefinition(newDefinition);
        definedReportsRepo.save(definition);
        return new Message("updated");
    }

    @DeleteMapping("/{id}")
    public Message DeleteDefinition(HttpServletRequest request,@PathVariable("id") Long id) throws ServletException{
        Optional<DefinedReports> opDefinition = definedReportsRepo.findById(id);
        if(!opDefinition.isPresent()){
            throw new ServletException("no such definition");
        }
        DefinedReports definition = opDefinition.get();
        if(!definition.getCourse().getMaster().getEmail().equals(request.getAttribute("subject").toString())){
            throw new ServletException("not yours");
        }
        definedReportsRepo.delete(definition);
        return new Message("deleted");
    }
    @GetMapping("/my")
    public Iterable<DefinedReports> myDefinitions(HttpServletRequest request){
        Users user = usersRepo.findByEmail(request.getAttribute("subject").toString());
        Iterable<Courses> myCourses = user.getCoursesSet();
        ArrayList<DefinedReports> myDefinedReports = new ArrayList<>();
        for (Courses course:myCourses) {
            myDefinedReports.addAll(course.getDefinedReports());
        }
        return myDefinedReports;
    }
}
