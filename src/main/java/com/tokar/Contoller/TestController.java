package com.tokar.Contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tokar.Entity.Users;
import com.tokar.Repository.UsersRepository;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class TestController {
@Autowired
private UsersRepository userRepo;


    @RequestMapping("/whoami")
    public Users getUser(HttpServletRequest request) {
        return userRepo.findByEmail(request.getAttribute("subject").toString());
    }

    @GetMapping("/mail/{mail}")
    public Users bymail(@PathVariable("mail") String mail){
        return userRepo.findByEmail(mail);
    }
}
