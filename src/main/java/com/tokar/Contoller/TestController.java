package com.tokar.Contoller;

import com.tokar.Entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tokar.Entity.Users;
import com.tokar.Repository.UsersRepository;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class TestController {
@Autowired
private UsersRepository UR;

    @RequestMapping("/")
    public Message greeting(@RequestParam(value = "name", defaultValue = "World") String name){
        return new Message("Hello "+name+"!");
    }

    @RequestMapping("/all")
    public Iterable<Users> getAllUsers() {
        // This returns a JSON or XML with the users
        return UR.findAll();
    }

    @GetMapping("/mail/{mail}")
    public Users bymail(@PathVariable("mail") String mail){
        return UR.findByEmail(mail);
    }
}
