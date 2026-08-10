package com.codewithfawaz.humanresourcemanagementsystem;

import com.codewithfawaz.humanresourcemanagementsystem.model.Role;
import com.codewithfawaz.humanresourcemanagementsystem.model.UserInputDto;
import com.codewithfawaz.humanresourcemanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HumanResourceManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HumanResourceManagementSystemApplication.class, args);
    }

}