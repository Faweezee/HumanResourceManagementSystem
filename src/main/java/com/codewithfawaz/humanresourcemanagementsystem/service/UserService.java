package com.codewithfawaz.humanresourcemanagementsystem.service;

import com.codewithfawaz.humanresourcemanagementsystem.model.UserInputDto;
import com.codewithfawaz.humanresourcemanagementsystem.model.UserLoginDto;
import com.codewithfawaz.humanresourcemanagementsystem.model.Users;
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

    public ResponseEntity<String> authenticate(UserLoginDto userDto) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        var user = userRepository.findByEmail(userDto.getEmail()).orElseThrow();
        if(auth.isAuthenticated())
            return ResponseEntity.ok("User Authorized\ntoken: "+jwtService.generateToken(user));
        return new ResponseEntity<>("User Not Authenticated", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<String> register(UserInputDto user) {
        List<Users> users = userRepository.findAll();
        for(Users person : users){
            if(person.getEmail().equals(user.getEmail()))
                return new ResponseEntity<>("User with email: "+user.getEmail()+" already exists", HttpStatus.OK);
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
        return new ResponseEntity<>(_user.getRole()+" - "+_user.getName()+" Created\ntoken: "+jwtService.generateToken(_user), HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteUserById(int id) {
        Optional<Users> user = userRepository.findById(id);
        if(!user.isPresent())
            return new ResponseEntity<>("USER NOT FOUND", HttpStatus.NOT_FOUND);
        userRepository.deleteById(id);
        return new ResponseEntity<>("USER:"+"\n\tID: "+user.get().getUserId() +
                "\n\tNAME: "+user.get().getName()+
                "\n\tEMAIL: "+user.get().getEmail()+"\nDELETED.", HttpStatus.OK);
    }
}
