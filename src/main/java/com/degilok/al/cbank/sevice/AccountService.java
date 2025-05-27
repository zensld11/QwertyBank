package com.degilok.al.cbank.sevice;

import com.degilok.al.cbank.entity.Account;

import java.util.List;

public interface AccountService {
    List<Account> getAccountsByUserUd(Long userId);
}
