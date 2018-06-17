package com.tokar.Contoller;

import com.tokar.DataPrototypes.Message;
import com.tokar.Entity.Users;
import com.tokar.DataPrototypes.UsersPrototype;
import com.tokar.Repository.UsersRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class UsersController {
    @Autowired
    UsersRepository userRepo;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Message jsonLogin(@RequestBody UsersPrototype login) throws ServletException {

        String jwtToken;
        String email = login.getEmail();
        String password = login.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            throw new ServletException("empty parameters");
        }

        Users user = userRepo.findByEmail(email);

        if (user == null) {
            throw new ServletException("invalid credentials");
        }

        if (!user.checkPassword(password)) {
            throw new ServletException("invalid credentials");
        }

        jwtToken = Jwts.builder().setSubject(email).claim("roles", user.getRoles()).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "c6208ec4cda9cdc4850310281c4703df").compact();

        return new Message(jwtToken);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Message jsonRegister (@RequestBody UsersPrototype newUser) throws ServletException{
        if(newUser.getEmail()==null || newUser.getName()==null || newUser.getLast_name()==null || newUser.getPassword()==null){
            throw new ServletException("empty parameters");
        }
        if(!Pattern.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@utp.edu.pl$",newUser.getEmail())){
            throw new ServletException("wrong domain");
        }
        Users user = userRepo.findByEmail(newUser.getEmail());
        if (user != null) {
            throw new ServletException("email already used");
        }
        user = new Users(newUser);
        userRepo.save(user);

        return new Message("registered");
    }

    @RequestMapping("/whoami")
    public Users getUser(HttpServletRequest request) {
        return userRepo.findByEmail(request.getAttribute("subject").toString());
    }
}
