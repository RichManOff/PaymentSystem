package com.example.paymentsystem2.controller;

import com.example.paymentsystem2.model.Account;
import com.example.paymentsystem2.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam BigDecimal initialBalance) {
        return accountService.createAccount(username, password, initialBalance);
    }

    @GetMapping("/{accountId}/balance")
    public BigDecimal getAccountBalance(@PathVariable Long accountId) {
        return accountService.getAccountBalance(accountId);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        accountService.login(username, password);
        return ResponseEntity.ok("Log in successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        accountService.logout();
        return ResponseEntity.ok("Logged out successfully");
    }
    // Add other account-related endpoints if needed
}
