package com.had.hospital_management.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private CustomUserDetailsService customUserDetailsService;
    private JwtAuthEntryPoint authEntryPoint;
    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService,JwtAuthEntryPoint authEntryPoint) {
        this.customUserDetailsService = customUserDetailsService;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println(http);
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        //auth
                        .requestMatchers("api/auth/**").permitAll()
                        .requestMatchers("user/get_by_id/**").permitAll()
                        .requestMatchers("user/get_all_doctor").permitAll()
                        .requestMatchers("user/get_all_lab").permitAll()

                        //appointment
                        .requestMatchers("/appointment/get_appointment_by_doctor_id/**").hasAuthority("DOCTOR")
                        .requestMatchers("/appointment/assign_lab/**").hasAuthority("DOCTOR")
                        .requestMatchers("/appointment/get_appointment_by_patient_id/**").hasAuthority("PATIENT")
                        .requestMatchers("/appointment/save").hasAuthority("PATIENT")
                        .requestMatchers("/appointment/get_appointment_by_lab_id/**").hasAuthority("LAB")
                        .requestMatchers("/appointment/update_doctor_status/**").hasAuthority("LAB")
                        .requestMatchers("/appointment/update_lab_status/**").hasAuthority("LAB")
                        .requestMatchers("/appointment/add_prescription/**").hasAuthority("LAB")
                        .requestMatchers("/appointment/add_lab_prescription/**").hasAuthority("LAB")

                        //requests
                        .requestMatchers("/requests/get_all_by_report_id/**").hasAnyAuthority("PATIENT","DOCTOR")
                        .requestMatchers("/requests/approve_request_by_id/**").hasAuthority("PATIENT")
                        .requestMatchers("/requests/save").hasAuthority("DOCTOR")
                        .requestMatchers("/requests/get_report_id_by_radiologist_id/**").hasAuthority("RADIOLOGIST")
                        .requestMatchers("requests/add_comment/**").hasAuthority("RADIOLOGIST")
                        .anyRequest().authenticated());

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public  JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
    // CORS Configuration
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true);
            }
        };
    }
}