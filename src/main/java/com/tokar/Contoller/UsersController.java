package com.tokar.Contoller;

import com.tokar.Entity.Message;
import com.tokar.Entity.Users;
import com.tokar.Entity.UsersLogin;
import com.tokar.Repository.UsersRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UsersController {
    @Autowired
    UsersRepository uRepo;

    @PostMapping(value = "/login/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Message jsonLogin(@RequestBody UsersLogin login) throws ServletException {

        String jwtToken = "";
        String email = login.getEmail();
        String password = login.getPassword();

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

        return new Message(jwtToken);
    }

    @PostMapping(value = "/login/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Message login(@RequestParam("email") String email, @RequestParam("password") String password ) throws ServletException {

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

        return new Message(jwtToken);
    }

    @PostMapping(value = "/register/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Message jsonRegister (@RequestBody Users newuser) throws ServletException{
        if(newuser.getEmail().isEmpty() || newuser.getName().isEmpty() || newuser.getLast_name().isEmpty() || newuser.getPassword().isEmpty()){
            throw new ServletException("Empty parameters!");
        }
        Users user = uRepo.findByEmail(newuser.getEmail());
        if (user != null) {
            throw new ServletException("Email already in use");
        }

        newuser.setRoles(1); //student
        uRepo.save(newuser);

        return new Message("registered");
    }

    @PostMapping(value = "/register/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Message register (
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

        return new Message("registered");
    }
}
