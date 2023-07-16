package com.example.paymentsystem2.controller;

import com.example.paymentsystem2.model.Account;
import com.example.paymentsystem2.model.Transaction;
import com.example.paymentsystem2.service.AccountService;
import com.example.paymentsystem2.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    @Autowired
    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction transferFunds(@RequestParam Long destinationAccountId, @RequestParam BigDecimal amount) {
        Account authenticatedUser = accountService.getAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new IllegalArgumentException("User not authenticated go to log in");
        }

        Long sourceAccountId = authenticatedUser.getId();
        return transactionService.transferFunds(sourceAccountId, destinationAccountId, amount);
    }


    // Add other transaction-related endpoints if needed
}
