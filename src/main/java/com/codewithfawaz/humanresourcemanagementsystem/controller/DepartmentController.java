package com.codewithfawaz.humanresourcemanagementsystem.controller;


import com.codewithfawaz.humanresourcemanagementsystem.model.Department;
import com.codewithfawaz.humanresourcemanagementsystem.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("all")
    public ResponseEntity<List<Department>> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable int id){
        return departmentService.getDepartmentById(id);
    }

    @PostMapping("add/{name}")
    public ResponseEntity<?> addDepartment(@PathVariable String name){
        return departmentService.addDepartment(name);
    }

    @PatchMapping("update")
    public ResponseEntity<?> updateDepartment(@RequestBody Department department){
        return departmentService.updateDepartment(department);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable int id){
        return departmentService.deleteDepartment(id);
    }
}