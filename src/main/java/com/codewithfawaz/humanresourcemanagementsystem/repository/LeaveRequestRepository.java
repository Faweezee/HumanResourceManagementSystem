package com.codewithfawaz.humanresourcemanagementsystem.repository;

import com.codewithfawaz.humanresourcemanagementsystem.model.Employee;
import com.codewithfawaz.humanresourcemanagementsystem.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findByEmployee(Employee employee);
}
