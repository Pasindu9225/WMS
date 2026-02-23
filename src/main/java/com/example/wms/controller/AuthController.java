package com.example.wms.controller;

import com.example.wms.security.JwtUtils;
import com.example.wms.security.LoginRequest;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtils jwtUtils;

    public AuthController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    // The @RequestBody tells Spring to look for JSON in the Postman Body
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String token = jwtUtils.generateToken(
                request.getUsername(),
                request.getGroupId(),
                request.getCompanyId()
        );
        return Map.of("token", token);
    }
}