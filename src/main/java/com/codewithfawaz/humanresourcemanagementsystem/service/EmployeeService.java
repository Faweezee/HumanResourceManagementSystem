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

    public ResponseEntity<?> addEmployee(EmployeeInputDto employee) {
        try {
            if(
                    employee.getFullName() == null || employee.getFullName().equals("string") ||
                    employee.getContactInfo() == null || employee.getContactInfo().equals("string") ||
                    employee.getStatus() == null ||
                    employee.getDateOfBirth() == null || /*employee.getDateOfBirth() == new Date() || //TODO  */
                    employee.getDepartmentId() == 0 || employee.getUserEmail() == null ||
                            employee.getUserEmail().equals("string")
            ){
                return new ResponseEntity<>(new Output(employee.getFullName()+" NOT saved.\nMAKE" +
                        " SURE ALL INFO IS INPUTTED"), HttpStatus.BAD_REQUEST);
            }
            Employee emp = new Employee();
            emp.setFullName(employee.getFullName());
            emp.setContactInfo(employee.getContactInfo());
            emp.setStatus(employee.getStatus());
            emp.setDateOfBirth(employee.getDateOfBirth());

            if(departmentRepository.findById(employee.getDepartmentId()).isEmpty())
                return new ResponseEntity<>(new Output("DEPARTMENT WITH ID: "+employee.getDepartmentId()+" NOT FOUND.\nMAKE SURE ALL INFO IS CORRECT"), HttpStatus.BAD_REQUEST);

            if(userRepository.findByEmail(employee.getUserEmail()).isEmpty())
                return new ResponseEntity<>(new Output("USER "+employee.getUserEmail()+" NOT FOUND.\nMAKE SURE ALL INFO IS CORRECT"), HttpStatus.BAD_REQUEST);

            emp.setDepartment(departmentRepository.findById(employee.getDepartmentId()).get());
            emp.setUser(userRepository.findByEmail(employee.getUserEmail()).get());

            employeeRepository.save(emp);
            return new ResponseEntity<>(new Output("EMPLOYEE: "+employee.getFullName()+", Saved."), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(employee.getFullName()+" NOT saved.", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteEmployeeById(Integer id) {
        Optional<Employee> emp = employeeRepository.findById(id);
        if (emp.isPresent()) {
            employeeRepository.deleteById(id);
            return new ResponseEntity<>(new Output(emp.get().getFullName()+" Deleted."), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(new Output("Employee with ID: "+id+" NOT FOUND"), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> findEmployeeById(int id){
        Optional<Employee> emp = employeeRepository.findById(id);
        if(emp.isEmpty())
            return new ResponseEntity<>(new Output("Employee with Id: "+id+" NOT FOUND"), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new EmployeeOutputDto(emp.get().getEmployeeId(), emp.get().getFullName(), emp.get().getContactInfo(), emp.get().getStatus(), emp.get().getDateOfBirth(), emp.get().getDepartment().getDepartmentName()), HttpStatus.OK);
    }

    public ResponseEntity<?> getEmployeesByDepartment(String departmenName) {
        Optional<Department> department = departmentRepository.findByDepartmentName(departmenName);
        if(department.isEmpty())
            return new ResponseEntity<>(new Output("Department: "+departmenName+" NOT FOUND"), HttpStatus.NOT_FOUND);
        List<EmployeeOutputDto> employees = new ArrayList<>();

        employeeRepository.findEmployeeByDepartment(department.get()).forEach(employee -> employees.add(new EmployeeOutputDto(employee.getEmployeeId(), employee.getFullName(), employee.getContactInfo(), employee.getStatus(), employee.getDateOfBirth(), employee.getDepartment().getDepartmentName()))
        );

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    public ResponseEntity<?> getEmployeesByDepartment(int id) {
        Optional<Department> department = departmentRepository.findById(id);
        if(department.isEmpty())
            return new ResponseEntity<>(new Output("Department with ID: "+id+" NOT FOUND"), HttpStatus.NOT_FOUND);

        List<EmployeeOutputDto> employees = new ArrayList<>();

        employeeRepository.findEmployeeByDepartment(department.get()).forEach(employee -> employees.add(new EmployeeOutputDto(employee.getEmployeeId(), employee.getFullName(), employee.getContactInfo(), employee.getStatus(), employee.getDateOfBirth(), employee.getDepartment().getDepartmentName()))
        );

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    public ResponseEntity<?> updateEmployeeById(int id, EmployeeInputDto emp) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(optionalEmployee.isEmpty()){
            return new ResponseEntity<>(new Output("EMPLOYEE WITH ID: "+id+" NOT saved.\n EMPLOYEE NOT FOUND"), HttpStatus.NOT_FOUND);
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
        if(emp.getUserEmail() != null || !emp.getUserEmail().equals("string") ){
            Optional<Users> user = userRepository.findByEmail(emp.getUserEmail());
            user.ifPresent(employee::setUser);
        }

        employeeRepository.save(employee);
        return new ResponseEntity<>(new EmployeeOutputDto(employee.getEmployeeId(), employee.getFullName(), employee.getContactInfo(), employee.getStatus(), employee.getDateOfBirth(), employee.getDepartment().getDepartmentName()), HttpStatus.CREATED);
    }
}