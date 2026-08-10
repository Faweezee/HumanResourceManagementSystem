package com.codewithfawaz.humanresourcemanagementsystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.codewithfawaz.humanresourcemanagementsystem.model.Permissions.*;
import static com.codewithfawaz.humanresourcemanagementsystem.model.Role.HR;
import static com.codewithfawaz.humanresourcemanagementsystem.model.Role.SUPER_ADMIN;
import static org.springframework.http.HttpMethod.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;
    private static final String[] WHITE_LIST_URL = {"/api/users/login",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(WHITE_LIST_URL)
                                .permitAll()

//                                .requestMatchers( "/api/users/**").hasAnyRole(SUPER_ADMIN.name(), HR.name())
                                .requestMatchers(POST, "/api/users/register").hasAnyAuthority(SUPER_ADMIN_CREATE.getPermission(), HR_CREATE.getPermission())
                                .requestMatchers(DELETE, "/api/users/**").hasAnyAuthority(SUPER_ADMIN_DELETE.getPermission(), HR_DELETE.getPermission())

                                .requestMatchers("/api/employees/**").hasAnyRole(SUPER_ADMIN.name(), HR.name())
                                .requestMatchers(GET,"/api/employees/**").hasAnyAuthority(SUPER_ADMIN_READ.getPermission(), HR_READ.getPermission())
                                .requestMatchers(POST,"/api/employees/**").hasAnyAuthority(SUPER_ADMIN_CREATE.getPermission(), HR_CREATE.getPermission())
                                .requestMatchers(PATCH,"/api/employees/**").hasAnyAuthority(SUPER_ADMIN_UPDATE.getPermission(), HR_UPDATE.getPermission())
                                .requestMatchers(DELETE,"/api/employees/**").hasAnyAuthority(SUPER_ADMIN_DELETE.getPermission(), HR_DELETE.getPermission())

                                .requestMatchers("/api/departments/**").hasAnyRole(SUPER_ADMIN.name(), HR.name())
                                .requestMatchers(GET,"/api/departments/**").hasAnyAuthority(SUPER_ADMIN_READ.getPermission(), HR_READ.getPermission())
                                .requestMatchers(POST,"/api/departments/**").hasAnyAuthority(SUPER_ADMIN_CREATE.getPermission(), HR_CREATE.getPermission())
                                .requestMatchers(PATCH,"/api/departments/**").hasAnyAuthority(SUPER_ADMIN_UPDATE.getPermission(), HR_UPDATE.getPermission())
                                .requestMatchers(DELETE,"/api/departments/**").hasAnyAuthority(SUPER_ADMIN_DELETE.getPermission(), HR_DELETE.getPermission())

                                .anyRequest().authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

}
