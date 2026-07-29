package com.codewithfawaz.humanresourcemanagementsystem.service;

import com.codewithfawaz.humanresourcemanagementsystem.model.*;
import com.codewithfawaz.humanresourcemanagementsystem.repository.DepartmentRepository;
import com.codewithfawaz.humanresourcemanagementsystem.repository.EmployeeRepository;
import com.codewithfawaz.humanresourcemanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public ResponseEntity<List<EmployeeOutputDto>> getAllEmployees() {
        List<EmployeeOutputDto> employees = new ArrayList<>();
        employeeRepository.findAll().forEach(
                employee -> employees.add(
                new EmployeeOutputDto(employee.getEmployeeId(), employee.getFullName(), employee.getContactInfo(), employee.getStatus(), employee.getDateOfBirth(), employee.getDepartment().getDepartmentName()
                )
        )
        );
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    public ResponseEntity<String> addEmployee(EmployeeInputDto employee) {
        try {
            if(
                    employee.getFullName() == null || employee.getFullName().equals("string") ||
                    employee.getContactInfo() == null || employee.getContactInfo().equals("string") ||
                    employee.getStatus() == null ||
                    employee.getDateOfBirth() == null || /*employee.getDateOfBirth() == new Date() || //TODO  */
                    employee.getDepartmentId() == 0 || employee.getUserId() == 0
            ){
                return new ResponseEntity<>(employee.getFullName()+" NOT saved.\nMAKE" +
                        " SURE ALL INFO IS INPUTED", HttpStatus.BAD_REQUEST);
            }
            Employee emp = new Employee();
            emp.setFullName(employee.getFullName());
            emp.setContactInfo(employee.getContactInfo());
            emp.setStatus(employee.getStatus());
            emp.setDateOfBirth(employee.getDateOfBirth());
            if(departmentRepository.findById(employee.getDepartmentId()).isEmpty())
                return new ResponseEntity<>("DEPARTMENT "+employee.getDepartmentId()+" NOT FOUND.\nMAKE SURE ALL INFO IS CORRECT", HttpStatus.BAD_REQUEST);
            if(userRepository.findById(employee.getUserId()).isEmpty())
                return new ResponseEntity<>("USER "+employee.getUserId()+" NOT FOUND.\nMAKE SURE ALL INFO IS CORRECT", HttpStatus.BAD_REQUEST);
            emp.setDepartment(departmentRepository.findById(employee.getDepartmentId()).get());
            emp.setUser(userRepository.findById(employee.getUserId()).get());

            employeeRepository.save(emp);
            return new ResponseEntity<>("EMPLOYEE: "+employee.getFullName()+", Saved.", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(employee.getFullName()+" NOT saved.", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> deleteEmployeeById(Integer id) {
        Optional<Employee> emp = employeeRepository.findById(id);
        if (emp.isPresent()) {
            employeeRepository.deleteById(id);
            return new ResponseEntity<>(emp.get().getFullName()+" Deleted.", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Employee with ID: "+id+" NOT FOUND", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> findEmployeeById(int id){
        Optional<Employee> emp = employeeRepository.findById(id);
        if(emp.isEmpty())
            return new ResponseEntity<>("Employee with Id: "+id+" NOT FOUND", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new EmployeeOutputDto(emp.get().getEmployeeId(), emp.get().getFullName(), emp.get().getContactInfo(), emp.get().getStatus(), emp.get().getDateOfBirth(), emp.get().getDepartment().getDepartmentName()), HttpStatus.OK);
    }

    public ResponseEntity<?> getEmployeesByDepartment(int departmentId) {
        Optional<Department> department = departmentRepository.findById(departmentId);
        if(department.isEmpty())
            return new ResponseEntity<>("Department with Id: "+departmentId+" NOT FOUND", HttpStatus.NOT_FOUND);
        List<EmployeeOutputDto> employees = new ArrayList<>();
        employeeRepository.findEmployeeByDepartment(department.get()).forEach(employee -> employees.add(new EmployeeOutputDto(employee.getEmployeeId(), employee.getFullName(), employee.getContactInfo(), employee.getStatus(), employee.getDateOfBirth(), employee.getDepartment().getDepartmentName()))
        );
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    public ResponseEntity<?> updateEmployeeById(int id, EmployeeInputDto emp) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(optionalEmployee.isEmpty()){
            return new ResponseEntity<>("EMPLOYEE WITH ID: "+id+" NOT saved.\n EMPLOYEE NOT FOUND", HttpStatus.NOT_FOUND);
        }
        Employee employee = optionalEmployee.get();
       if(emp.getFullName() != null && !emp.getFullName().equals("string")){
           employee.setFullName(emp.getFullName());
       }
        if(emp.getDateOfBirth() != null /*&& employee.getDateOfBirth() != new Date() //TODO  */){
            employee.setDateOfBirth(emp.getDateOfBirth());
        }
        if(emp.getContactInfo() != null && !emp.getContactInfo().equals("string")){
            employee.setContactInfo(emp.getContactInfo());
        }
        if(emp.getStatus() != null ){
            employee.setStatus(emp.getStatus());
        }
        if(emp.getDepartmentId() != 0 ){
            Optional<Department> department = departmentRepository.findById(emp.getDepartmentId());
            department.ifPresent(employee::setDepartment);
        }
        if(emp.getUserId() != 0 ){
            Optional<Users> user = userRepository.findById(emp.getUserId());
            user.ifPresent(employee::setUser);
        }

        employeeRepository.save(employee);
        return new ResponseEntity<>(new EmployeeOutputDto(employee.getEmployeeId(), employee.getFullName(), employee.getContactInfo(), employee.getStatus(), employee.getDateOfBirth(), employee.getDepartment().getDepartmentName()), HttpStatus.CREATED);
    }
}
