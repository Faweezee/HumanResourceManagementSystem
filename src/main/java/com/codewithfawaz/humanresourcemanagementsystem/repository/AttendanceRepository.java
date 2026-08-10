package com.codewithfawaz.humanresourcemanagementsystem.repository;

import com.codewithfawaz.humanresourcemanagementsystem.model.AttendanceRecord;
import com.codewithfawaz.humanresourcemanagementsystem.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Integer> {
    List<AttendanceRecord> findByDateAndClockOutTime(LocalDate date, LocalTime clockOutTime);

    List<AttendanceRecord> findByDate(LocalDate date);

    Optional<AttendanceRecord> findByEmployeeAndDate(Employee employee, LocalDate date);
}