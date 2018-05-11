package com.tokar.Repository;

import org.springframework.data.repository.CrudRepository;
import com.tokar.Entity.Users;

public interface UsersRepository extends CrudRepository<Users, Long> {
    public Users findByEmail(String email);

}
