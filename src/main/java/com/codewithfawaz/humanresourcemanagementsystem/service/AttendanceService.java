package com.codewithfawaz.humanresourcemanagementsystem.service;

import com.codewithfawaz.humanresourcemanagementsystem.model.*;
import com.codewithfawaz.humanresourcemanagementsystem.repository.AttendanceRepository;
import com.codewithfawaz.humanresourcemanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    private Employee getCurrentEmployee() throws RuntimeException {
        String email = "";
        try {
            email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Users currentUser = userRepository.findByEmail(email).get();
        return currentUser.getEmployee();
    }
    private List<AttendanceRecord> getUncheckedRecords(LocalDate date) {
        List<AttendanceRecord> attendanceRecords = attendanceRepository.findByDate(date);
        attendanceRecords = attendanceRecords.stream().filter(attendanceRecord -> attendanceRecord.getClockOutTime() == null).collect(Collectors.toList());
        return attendanceRecords;
    }

    public ResponseEntity<?> employeeClockIn() {
        Employee employee = getCurrentEmployee();
        LocalDate today = LocalDate.now();
        Optional<AttendanceRecord> attendanceRecord = attendanceRepository.findByEmployeeAndDate(employee, today);
        if (attendanceRecord.isPresent())
            return new ResponseEntity<>(new Output("clocked in"), HttpStatus.BAD_REQUEST);
        AttendanceRecord record = new AttendanceRecord();
        record.setDate(today);
        record.setClockInTime(LocalTime.now());
        record.setEmployee(employee);
        attendanceRepository.save(record);
        return new ResponseEntity<>(new Output("clocked in"), HttpStatus.CREATED);
    }

    public ResponseEntity<?> employeeClockOut() {
        Employee employee = getCurrentEmployee();
        LocalDate today = LocalDate.now();
        Optional<AttendanceRecord> attendanceRecord = attendanceRepository.findByEmployeeAndDate(employee, today);
        if (attendanceRecord.isEmpty()) {
            return new ResponseEntity<>(new Output("not clocked in"), HttpStatus.BAD_REQUEST);
        }
        if (attendanceRecord.get().getClockOutTime() != null)
            return new ResponseEntity<>(new Output("clocked out"), HttpStatus.OK);

        AttendanceRecord record = attendanceRecord.get();
        record.setClockOutTime(LocalTime.now());
        record.setStatus(AttendanceStatus.PRESENT);
        attendanceRepository.save(record);
        return new ResponseEntity<>(new Output("clocked out"), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAttendanceStatus() {
        Employee employee = getCurrentEmployee();
        LocalDate today = LocalDate.now();
        Optional<AttendanceRecord> attendanceRecord = attendanceRepository.findByEmployeeAndDate(employee, today);
        if (attendanceRecord.isEmpty()) {
            return new ResponseEntity<>(new Output("not clocked in"), HttpStatus.OK);
        }
        if (attendanceRecord.get().getClockOutTime() != null)
            return new ResponseEntity<>(new Output("clocked out"), HttpStatus.OK);
        return new ResponseEntity<>(new Output("clocked in"), HttpStatus.OK);
    }
}