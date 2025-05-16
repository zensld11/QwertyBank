package com.degilok.al.cbank.controller;

import com.degilok.al.cbank.entity.dto.UserDto;
import com.degilok.al.cbank.sevice.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestParam UserDto userDto) throws Exception {
        userService.createUser(userDto);
        return ResponseEntity.ok("Пользователь создан");
    }

    @GetMapping("/doesntHasAccess")
    public ResponseEntity<String> accessDenied() {
        return ResponseEntity.badRequest().build();
    }
}