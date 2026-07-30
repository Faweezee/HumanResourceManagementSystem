package com.codewithfawaz.humanresourcemanagementsystem.controller;

import com.codewithfawaz.humanresourcemanagementsystem.model.EmployeeInputDto;
import com.codewithfawaz.humanresourcemanagementsystem.model.EmployeeOutputDto;
import com.codewithfawaz.humanresourcemanagementsystem.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("all")
    public ResponseEntity<List<EmployeeOutputDto>> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable int id){
        return employeeService.findEmployeeById(id);
    }

    @GetMapping("department")
    public ResponseEntity<?> getEmployeesByDepartment(@RequestParam String name){
        return employeeService.getEmployeesByDepartment(name);
    }

    @GetMapping("department/id")
    public ResponseEntity<?> getEmployeesByDepartment(@RequestParam int id){
        return employeeService.getEmployeesByDepartment(id);
    }

    @PatchMapping("update/{id}")
    public ResponseEntity<?> updateEmployeeById(@PathVariable int id, @RequestBody EmployeeInputDto emp){
        return employeeService.updateEmployeeById(id, emp);
    }

    @PostMapping("add")
    public ResponseEntity<String> addEmployee(@RequestBody EmployeeInputDto employee){
        return employeeService.addEmployee(employee);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable Integer id){
        return employeeService.deleteEmployeeById(id);
    }
}
