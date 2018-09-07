package com.tokar.Repository;

import com.tokar.Entity.Users;
import org.springframework.data.repository.CrudRepository;
import com.tokar.Entity.Courses;

import javax.jws.soap.SOAPBinding;
import java.util.Optional;

public interface CoursesRepository extends CrudRepository<Courses, Long>{
    Optional<Courses> findByAccesskey(String access_key);
    Iterable<Courses> findAllByMaster(Users master);
}
