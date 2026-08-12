package com.codewithfawaz.humanresourcemanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOutput {
    private String message;
    private String token;
    private String username;
    private Role role;
}
