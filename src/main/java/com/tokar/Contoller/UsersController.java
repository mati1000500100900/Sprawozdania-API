package com.tokar.Contoller;

import com.tokar.Entity.Users;
import com.tokar.Repository.UsersRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

        if (email == null || password == null) {
            throw new ServletException("Please fill in username and password");
        }

        Users user = uRepo.findByEmail(email);

        if (user == null) {
            throw new ServletException("User email not found.");
        }

        if (!user.checkPassword(password)) {
            throw new ServletException("Invalid login. Please check your name and password."+password);
        }

        jwtToken = Jwts.builder().setSubject(email).claim("roles", "user").setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();

        return jwtToken;
    }
}
