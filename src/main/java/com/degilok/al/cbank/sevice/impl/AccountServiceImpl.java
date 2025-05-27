package com.degilok.al.cbank.sevice.impl;

import com.degilok.al.cbank.entity.Account;
import com.degilok.al.cbank.exception.UserIdNotFoundException;
import com.degilok.al.cbank.repository.AccountRepository;
import com.degilok.al.cbank.sevice.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> getAccountsByUserUd(Long userId) {
        try {
            return accountRepository.findUserById(userId);
        } catch (IllegalArgumentException e) {
            throw new UserIdNotFoundException(e.getMessage());
        }
    }
}
