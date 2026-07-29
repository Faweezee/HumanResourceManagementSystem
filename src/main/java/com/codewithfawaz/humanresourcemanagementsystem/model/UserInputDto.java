package com.codewithfawaz.humanresourcemanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInputDto {

    private String email;
    private String password;
    private String name;
    private Role role;
}
