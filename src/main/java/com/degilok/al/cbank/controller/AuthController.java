package com.degilok.al.cbank.controller;

import com.degilok.al.cbank.config.SecurityConfig;
import com.degilok.al.cbank.entity.dto.LoginDto;
import com.degilok.al.cbank.sevice.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final SecurityConfig securityConfig;

    public AuthController(AuthService authService, SecurityConfig securityConfig) {
        this.authService = authService;
        this.securityConfig = securityConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) throws Exception {
             String result = authService.login(loginDto.getUsername(), loginDto.getPassword());
        return ResponseEntity.ok().body("Bearer " + result);
    }
}
