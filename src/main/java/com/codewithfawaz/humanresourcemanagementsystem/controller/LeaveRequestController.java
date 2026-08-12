package com.codewithfawaz.humanresourcemanagementsystem.controller;

import com.codewithfawaz.humanresourcemanagementsystem.model.LeaveRequestInputDto;
import com.codewithfawaz.humanresourcemanagementsystem.model.LeaveRequestOutputDto;
import com.codewithfawaz.humanresourcemanagementsystem.model.LeaveStatus;
import com.codewithfawaz.humanresourcemanagementsystem.model.Role;
import com.codewithfawaz.humanresourcemanagementsystem.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('employee:create', 'manager:create', 'hr:create')")
    public ResponseEntity<?> submitLeaveRequest(@RequestBody LeaveRequestInputDto leaveRequestInput){
        return leaveRequestService.submitLeaveRequest(leaveRequestInput);
    }

    @GetMapping("my-leaves")
    @PreAuthorize("hasAnyAuthority('employee:read', 'manager:read', 'hr:read')")
    public ResponseEntity<List<LeaveRequestOutputDto>> getEmployeeLeaveRequests(){
        return leaveRequestService.getEmployeeLeaveRequests();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('hr:read')")
    public ResponseEntity<?> getAllLeaveRequests(){
//        List<LeaveRequestOutputDto>
        return leaveRequestService.getAllLeaveRequests();
    }

    @PatchMapping("{id}/status")
    @PreAuthorize("hasAnyAuthority('hr:update')")
    public ResponseEntity<?> changeLeaveStatus(@PathVariable int id, @RequestBody LeaveStatus leaveStatus){
//        LeaveRequestOutputDto
        return leaveRequestService.changeLeaveStatus(id, leaveStatus);
    }
}