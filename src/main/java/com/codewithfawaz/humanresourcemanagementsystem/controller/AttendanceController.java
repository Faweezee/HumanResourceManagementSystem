package com.codewithfawaz.humanresourcemanagementsystem.controller;

import com.codewithfawaz.humanresourcemanagementsystem.repository.UserRepository;
import com.codewithfawaz.humanresourcemanagementsystem.service.AttendanceService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("clock-in")
    @PreAuthorize("hasAnyAuthority('employee:create', 'manager:create', 'hr:create')")
    public ResponseEntity<?> employeeClockIn(){
        return attendanceService.employeeClockIn();
    }

    @PatchMapping("clock-out")
    @PreAuthorize("hasAnyAuthority('employee:update', 'manager:update', 'hr:update')")
    public ResponseEntity<?> employeeClockOut() {
        return attendanceService.employeeClockOut();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('employee:read', 'manager:read', 'hr:read')")
    public ResponseEntity<?> getAttendanceStatus(){
        return attendanceService.getAttendanceStatus();
    }
}