package com.tokar.Repository;

import org.springframework.data.repository.CrudRepository;
import com.tokar.Entity.Courses;

import java.util.Optional;

public interface CoursesRepository extends CrudRepository<Courses, Long>{
    public Optional<Courses> findByAccesskey(String access_key);
}
