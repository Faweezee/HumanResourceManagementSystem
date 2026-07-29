package com.codewithfawaz.humanresourcemanagementsystem.repository;

import com.codewithfawaz.humanresourcemanagementsystem.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findUsersByEmail(String email);
}
