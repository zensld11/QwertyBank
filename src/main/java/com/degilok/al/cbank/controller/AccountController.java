package com.degilok.al.cbank.controller;

import com.degilok.al.cbank.sevice.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/getId")
    public ResponseEntity<String> getAccountsByUserId(@RequestParam("userId") Long userId) {
        accountService.getAccountsByUserUd(userId);
        return ResponseEntity.ok("User by userId: " + userId);
    }
}