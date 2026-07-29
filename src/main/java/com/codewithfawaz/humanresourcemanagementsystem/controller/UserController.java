package com.codewithfawaz.humanresourcemanagementsystem.controller;

import com.codewithfawaz.humanresourcemanagementsystem.model.UserInputDto;
import com.codewithfawaz.humanresourcemanagementsystem.model.UserLoginDto;
import com.codewithfawaz.humanresourcemanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    @SecurityRequirements
    public ResponseEntity<String> registerUser(@RequestBody UserInputDto user){
        return userService.register(user);
    }

    @PostMapping("login")
    @SecurityRequirements
    public ResponseEntity<String> verifyUser(@RequestBody UserLoginDto user){
        return userService.authenticate(user);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable int id){
        return userService.deleteUserById(id);
    }

    @GetMapping("test")
    public String test(){
        return "Success";
    }
}
