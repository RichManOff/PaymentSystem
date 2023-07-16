package com.example.paymentsystem2.service;

import com.example.paymentsystem2.model.Account;
import com.example.paymentsystem2.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testCreateAccount() {
        String username = "testUser";
        String password = "testPassword";
        BigDecimal initialBalance = BigDecimal.valueOf(100.0);

        Account createdAccount = accountService.createAccount(username, password, initialBalance);

        assertEquals(username, createdAccount.getUsername());
        assertEquals(password, createdAccount.getPassword());
        assertEquals(initialBalance, createdAccount.getBalance());

        verify(accountRepository, times(1)).save(any(Account.class));
    }
}
