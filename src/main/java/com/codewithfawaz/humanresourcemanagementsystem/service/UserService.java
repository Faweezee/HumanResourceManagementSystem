package com.codewithfawaz.humanresourcemanagementsystem.service;

import com.codewithfawaz.humanresourcemanagementsystem.model.*;
import com.codewithfawaz.humanresourcemanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> authenticate(UserLoginDto userDto) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        var user = userRepository.findByEmail(userDto.getEmail()).orElseThrow();
        if(auth.isAuthenticated())
            return ResponseEntity.ok(new UserOutput("authorized", ""+jwtService.generateToken(user), user.getUsername(), user.getRole(), user.getName()));

        return new ResponseEntity<>(new Output("not authorized"), HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> register(UserInputDto user) {
        List<Users> users = userRepository.findAll();
        for(Users person : users){
            if(person.getEmail().equals(user.getEmail()))
                return new ResponseEntity<>(new Output("user already exists"), HttpStatus.OK);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var _user = Users
                .builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                        .role(user.getRole())
                                .build();
        userRepository.save(_user);
        return new ResponseEntity<>(new UserOutput("user created", ""+jwtService.generateToken(_user), _user.getUsername(), _user.getRole(), _user.getName()), HttpStatus.CREATED);
    }

    public ResponseEntity<?> deleteUserById(int id) {
        Optional<Users> user = userRepository.findById(id);
        if(!user.isPresent())
            return new ResponseEntity<>(new Output("user not found"), HttpStatus.NOT_FOUND);
        userRepository.deleteById(id);
        return new ResponseEntity<>(new Output("user "+user.get().getName()+" deleted"), HttpStatus.OK);
    }
}
