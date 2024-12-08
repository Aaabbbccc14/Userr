package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    ///
    @Autowired
    private final AtomicReference<PasswordEncoder> passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthController() {
        passwordEncoder = new AtomicReference<PasswordEncoder>();
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.get().encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User foundUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.get().matches(user.getPassword(), foundUser.getPassword())) {
            return jwtUtil.generateToken(foundUser.getUsername());
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}

