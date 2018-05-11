package com.tokar.Contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.tokar.Entity.Courses;
import com.tokar.Repository.CursesRepository;

import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CoursesController {
    @Autowired
    private CursesRepository CR;

    @GetMapping
    public Iterable<Courses> GetAll(){
    return CR.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Courses> getCourse(@PathVariable("id") int id){
    return CR.findById(Long.valueOf(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insertCourse(@RequestBody Courses course){
        CR.save(course);
    }

}
