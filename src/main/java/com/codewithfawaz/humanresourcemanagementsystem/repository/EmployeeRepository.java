package com.codewithfawaz.humanresourcemanagementsystem.repository;

import com.codewithfawaz.humanresourcemanagementsystem.model.Department;
import com.codewithfawaz.humanresourcemanagementsystem.model.Employee;
import com.codewithfawaz.humanresourcemanagementsystem.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findEmployeeByDepartment(Department department);

    Optional<Employee> findByUser(Users user);
}