package com.codewithfawaz.humanresourcemanagementsystem.service;

import com.codewithfawaz.humanresourcemanagementsystem.model.*;
import com.codewithfawaz.humanresourcemanagementsystem.repository.EmployeeRepository;
import com.codewithfawaz.humanresourcemanagementsystem.repository.LeaveRequestRepository;
import com.codewithfawaz.humanresourcemanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;

    private Employee getCurrentEmployee() throws RuntimeException{
        String email = "";
        try{
            email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Users currentUser = userRepository.findByEmail(email).get();
        return currentUser.getEmployee();
    }

    public ResponseEntity<String> submitLeaveRequest(LeaveRequestInputDto leaveRequestInput) {
        Employee employee = getCurrentEmployee();

        if (leaveRequestInput.getStartDate().before(new Date())){
            return new ResponseEntity<>("START DATE CANNOT BE IN THE PAST!", HttpStatus.BAD_REQUEST);
        }
        if (leaveRequestInput.getEndDate().before(leaveRequestInput.getStartDate())){
            return new ResponseEntity<>("END DATE CANNOT BE AFTER START DATE!", HttpStatus.BAD_REQUEST);
        }
        LeaveStatus status = LeaveStatus.PENDING;
        LeaveRequest request = new LeaveRequest();
        request.setLeaveStatus(status);
        request.setLeaveType(leaveRequestInput.getLeaveType());
        request.setEndDate(leaveRequestInput.getEndDate());
        request.setStartDate(leaveRequestInput.getStartDate());
        request.setEmployee(employee);
        request.setReason(leaveRequestInput.getReason());
        request = leaveRequestRepository.save(request);
        return new ResponseEntity<>("LEAVE REQUEST CREATED", HttpStatus.OK);
    }

    public ResponseEntity<List<LeaveRequestOutputDto>> getEmployeeLeaveRequests() {
        Employee employee = getCurrentEmployee();
        List<LeaveRequestOutputDto> requestList = new ArrayList<>();
        leaveRequestRepository.findByEmployee(employee).forEach((leaveRequest -> requestList.add(new LeaveRequestOutputDto(leaveRequest.getLeaveId(), leaveRequest.getReason(), leaveRequest.getLeaveStatus(), leaveRequest.getLeaveType(), leaveRequest.getEmployee().getFullName(), leaveRequest.getEmployee().getDepartment().getDepartmentName(), leaveRequest.getStartDate(), leaveRequest.getEndDate()))));
        return ResponseEntity.ok(requestList);
    }

    public ResponseEntity<List<LeaveRequestOutputDto>> getAllLeaveRequests() {
        List<LeaveRequestOutputDto> requestList = new ArrayList<>();
        leaveRequestRepository.findAll().forEach( leaveRequest ->
                requestList.add( new LeaveRequestOutputDto(
                        leaveRequest.getLeaveId(), leaveRequest.getReason(), leaveRequest.getLeaveStatus(), leaveRequest.getLeaveType(), leaveRequest.getEmployee().getFullName(), leaveRequest.getEmployee().getDepartment().getDepartmentName(), leaveRequest.getStartDate(), leaveRequest.getEndDate()
                ))
        );
        return ResponseEntity.ok(requestList);
    }

    public ResponseEntity<?> changeLeaveStatus(int id, LeaveStatus leaveStatus) {
        Optional<LeaveRequest> optionalLeaveRequest = leaveRequestRepository.findById(id);
        if(optionalLeaveRequest.isEmpty())
            return new ResponseEntity<>("REQUEST WITH ID: "+id+" NOT FOUND", HttpStatus.NOT_FOUND);
        LeaveRequest leaveRequest = optionalLeaveRequest.get();
        leaveRequest.setLeaveStatus(leaveStatus);
        leaveRequestRepository.save( leaveRequest);
        return ResponseEntity.ok(new LeaveRequestOutputDto(leaveRequest.getLeaveId(), leaveRequest.getReason(), leaveRequest.getLeaveStatus(), leaveRequest.getLeaveType(), leaveRequest.getEmployee().getFullName(), leaveRequest.getEmployee().getDepartment().getDepartmentName(), leaveRequest.getStartDate(), leaveRequest.getEndDate()));
    }
}