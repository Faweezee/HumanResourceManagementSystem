package com.codewithfawaz.humanresourcemanagementsystem.config;

import com.codewithfawaz.humanresourcemanagementsystem.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class ApplicationConfiguration {

    private final UserRepository userRepository;

    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findUsersByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
