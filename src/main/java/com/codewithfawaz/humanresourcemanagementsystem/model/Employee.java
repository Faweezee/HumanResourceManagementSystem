package com.codewithfawaz.humanresourcemanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer employeeId;
    private String fullName;
    private String contactInfo;
    private WorkStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    @ManyToOne
    private Department department;

    @OneToOne
    private Users user;

    @OneToMany(mappedBy = "employee")
    private List<LeaveRequest> leaveRequestList;
    @OneToMany(mappedBy = "employee")
    private List<AttendanceRecord> attendanceRecordList;
}