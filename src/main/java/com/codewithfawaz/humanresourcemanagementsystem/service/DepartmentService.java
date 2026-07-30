package com.codewithfawaz.humanresourcemanagementsystem.service;

import com.codewithfawaz.humanresourcemanagementsystem.model.Department;
import com.codewithfawaz.humanresourcemanagementsystem.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;


    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentRepository.findAll());
    }

    public ResponseEntity<?> addDepartment(String name) {
        if(name == null || name.equals("string")){
            return new ResponseEntity<>("PLEASE INPUT A DEPARTMENT NAME", HttpStatus.BAD_REQUEST);
        }
        Department department = new Department();
        department.setDepartmentName(name);
        return new ResponseEntity<>("New Department Saved:\n"+departmentRepository.save(department).getDepartmentId()+", "+department.getDepartmentName(), HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateDepartment(Department department) {
        if(departmentRepository.findById(department.getDepartmentId()).isEmpty()){
            return new ResponseEntity<>("PLEASE INPUT A DEPARTMENT NAME", HttpStatus.BAD_REQUEST);
        }

        if (department.getDepartmentName() != null && !department.getDepartmentName().equals("string")){
            return new ResponseEntity<>("Department Updated:\n"+departmentRepository.save(department).getDepartmentId()+", "+department.getDepartmentName(), HttpStatus.OK);
        }
        return new ResponseEntity<>("PLEASE INPUT ALL DATA PROPERLY", HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<String> deleteDepartment(int id) {
        if(departmentRepository.findById(id).isEmpty())
            return new ResponseEntity<>("DEPARTMENT WITH ID: "+id+" DOES NOT EXIST", HttpStatus.BAD_REQUEST);
        departmentRepository.deleteById(id);
        return new ResponseEntity<>("DEPARTMENT DELETED, ID: "+id, HttpStatus.OK);
    }

    public ResponseEntity<?> getDepartmentById(int id) {
        Optional<Department> department = departmentRepository.findById(id);
        if(department.isEmpty())
            return new ResponseEntity<>("DEPARTMENT NOT FOUND", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(department.get(), HttpStatus.OK);
    }
}
