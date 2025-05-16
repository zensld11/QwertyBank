package com.degilok.al.cbank.controller;

import com.degilok.al.cbank.entity.User;
import com.degilok.al.cbank.entity.dto.UserDto;
import com.degilok.al.cbank.sevice.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check")
    public String registerForm() {
        return "registration";
    }

    //@RequestBody — для отправки JSON
    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody User user) throws Exception {
        userService.registerUser(user);
        return ResponseEntity.ok("Пользователь зарегистрирован");
    }
}