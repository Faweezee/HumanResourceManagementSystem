package com.codewithfawaz.humanresourcemanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer attendanceRecordId;
    private LocalDate date;
    private LocalTime clockInTime;
    private LocalTime clockOutTime;
    private AttendanceStatus status;

    @ManyToOne
    private Employee employee;
}