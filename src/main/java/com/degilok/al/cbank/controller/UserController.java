package com.degilok.al.cbank.controller;

import com.degilok.al.cbank.entity.dto.UserDto;
import com.degilok.al.cbank.sevice.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody UserDto userDto) throws Exception {
        userService.createUser(userDto);
        return ResponseEntity.ok("Пользователь создан");
    }

    @PostMapping("/update/user")
    public ResponseEntity<String> update(@RequestBody UserDto userDto) {
        userService.updateUser(userDto);
        return ResponseEntity.ok("Данные пользователя обновлены");
    }

    @GetMapping("/doesntHasAccess")
    public ResponseEntity<String> accessDenied() {
        return ResponseEntity.badRequest().build();
    }
}