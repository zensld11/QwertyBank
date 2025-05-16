package com.degilok.al.cbank.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    String name;
    String email;
    String password;
    String role;
    LocalDateTime createdAt;

    public UserDto(String name, String email, String password, String role, LocalDateTime createdAt) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }
}
