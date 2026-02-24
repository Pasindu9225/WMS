package com.example.wms.controller;

import com.example.wms.domain.Role;
import com.example.wms.domain.User;
import com.example.wms.repository.RoleRepository;
import com.example.wms.repository.UserRepository;
import com.example.wms.security.JwtUtils;
import com.example.wms.security.LoginRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtUtils jwtUtils, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public String register(@RequestBody Map<String, String> request) {
        User user = new User();
        user.setUsername(request.get("username"));
        user.setPassword(passwordEncoder.encode(request.get("password")));
        user.setGroupId(request.get("groupId"));
        user.setCompanyId(request.get("companyId"));

        String roleName = request.get("role");
        if (roleName == null) {
            throw new RuntimeException("Error: Role must be specified in the request.");
        }

        Role assignedRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Error: Role '" + roleName + "' not found."));

        user.setRoles(Set.of(assignedRole));

        userRepository.save(user);
        return "User registered successfully with " + roleName + " permissions!";
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

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