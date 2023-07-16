package com.example.paymentsystem2.service;

import com.example.paymentsystem2.model.Account;
import com.example.paymentsystem2.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService{

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    private Account authenticatedUser;

    public Account getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void login(String username, String password) {
        Account account = accountRepository.findByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            authenticatedUser = account;
        }
    }

    public void logout() {
        authenticatedUser = null;
    }

    @Transactional
    public Account createAccount(String username, String password, BigDecimal initialBalance) {
        Account existingAccount = accountRepository.findByUsername(username);
        if (existingAccount != null) {
            throw new IllegalArgumentException("An account with this username already exists.");
        }

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setBalance(initialBalance);
        return accountRepository.save(account);
    }

    public BigDecimal getAccountBalance(Long accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        return optionalAccount.map(Account::getBalance).orElse(BigDecimal.ZERO);
    }

}
