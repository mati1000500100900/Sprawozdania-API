package com.tokar.Contoller;

import com.tokar.Entity.Message;
import com.tokar.Entity.Users;
import com.tokar.DataPrototypes.UsersPrototype;
import com.tokar.Repository.UsersRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.util.Date;

@RestController
@RequestMapping("/user")
public class UsersController {
    @Autowired
    UsersRepository uRepo;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Message jsonLogin(@RequestBody UsersPrototype login) throws ServletException {

        String jwtToken = "";
        String email = login.getEmail();
        String password = login.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            throw new ServletException("please fill in username and password");
        }

        Users user = uRepo.findByEmail(email);

        if (user == null) {
            throw new ServletException("user email not found.");
        }

        if (!user.checkPassword(password)) {
            throw new ServletException("invalid credentials");
        }

        jwtToken = Jwts.builder().setSubject(email).claim("roles", user.getRoles()).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();

        return new Message(jwtToken);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Message jsonRegister (@RequestBody Users newuser) throws ServletException{
        if(newuser.getEmail().isEmpty() || newuser.getName().isEmpty() || newuser.getLast_name().isEmpty() || newuser.getPassword().isEmpty()){
            throw new ServletException("empty parameterse");
        }
        Users user = uRepo.findByEmail(newuser.getEmail());
        if (user != null) {
            throw new ServletException("email already used");
        }

        newuser.setRoles(1); //student
        uRepo.save(newuser);

        return new Message("registered");
    }
}
