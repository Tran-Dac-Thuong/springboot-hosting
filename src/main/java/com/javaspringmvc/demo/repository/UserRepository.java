package com.javaspringmvc.demo.repository;

import com.javaspringmvc.demo.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByEmail(String email);

    public boolean existsByEmail(String email);

    public User findByUsername(String username);

    public boolean existsByFirstNameAndLastName(String firstName, String lastName);

//    public User getCustomerByEmailAndName(String email,String name);


}
