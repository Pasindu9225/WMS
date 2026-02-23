package com.example.wms.controller;

import com.example.wms.domain.User;
import com.example.wms.repository.UserRepository;
import com.example.wms.security.JwtUtils;
import com.example.wms.security.LoginRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public String register(@RequestBody User user) {
        // Requirement 4: Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Requirement 77: Extract roles to include in the JWT
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());

        // Requirement 15: Generate Token with Tenant IDs AND Roles
        String accessToken = jwtUtils.generateToken(
                user.getUsername(),
                user.getGroupId(),
                user.getCompanyId(),
                roles
        );

        String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }
}