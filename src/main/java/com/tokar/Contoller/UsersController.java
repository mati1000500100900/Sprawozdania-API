package com.tokar.Contoller;

import com.tokar.Entity.Users;
import com.tokar.Repository.UsersRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletException;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UsersController {
    @Autowired
    UsersRepository uRepo;

    @PostMapping(value = "/login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String login(@RequestParam("email") String email, @RequestParam("password") String password ) throws ServletException {

        String jwtToken = "";

        if (email.isEmpty() || password.isEmpty()) {
            throw new ServletException("Please fill in username and password");
        }

        Users user = uRepo.findByEmail(email);

        if (user == null) {
            throw new ServletException("User email not found.");
        }

        if (!user.checkPassword(password)) {
            throw new ServletException("Invalid login. Please check your name and password.");
        }

        jwtToken = Jwts.builder().setSubject(email).claim("roles", user.getRoles()).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();

        return jwtToken;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String register (
            @RequestParam("email") String email,
            @RequestParam("name") String name,
            @RequestParam("last_name") String last_name,
            @RequestParam("password") String password
    ) throws ServletException{
        if(email.isEmpty() || name.isEmpty() || last_name.isEmpty() || password.isEmpty()){
            throw new ServletException("Empty parameters!");
        }
        Users user = uRepo.findByEmail(email);
        if (user != null) {
            throw new ServletException("Email already in use");
        }

        user = new Users();
        user.setEmail(email);
        user.setName(name);
        user.setLast_name(last_name);
        user.setPassword(password);
        user.setRoles(1); //student
        uRepo.save(user);

        return "registered";
    }
}
