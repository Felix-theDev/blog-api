package com.felix.blog.controller;

import com.felix.blog.entity.User;
import com.felix.blog.repository.UserRepository;
import com.felix.blog.security.JwtUtil;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {
        var user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .enabled(true)
                .build();
        userRepo.save(user);
        return "User registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        return jwtUtil.generateToken(req.getUsername());
    }

    @Data static class AuthRequest {
        private String username;
        private String password;
    }

    @Data static class RegisterRequest {
        private String username;
        private String password;
        private User.Role role;
    }
}