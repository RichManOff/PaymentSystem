package com.example.paymentsystem2.service;

import com.example.paymentsystem2.model.Account;
import com.example.paymentsystem2.model.Transaction;
import com.example.paymentsystem2.repository.AccountRepository;
import com.example.paymentsystem2.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void testTransferFunds_SufficientBalance() {
        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(BigDecimal.valueOf(100.0));

        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setBalance(BigDecimal.valueOf(50.0));

        BigDecimal amountToTransfer = BigDecimal.valueOf(30.0);

        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        Mockito.when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));

        Transaction transaction = transactionService.transferFunds(1L, 2L, amountToTransfer);

        assertEquals(BigDecimal.valueOf(70.0), sourceAccount.getBalance());
        assertEquals(BigDecimal.valueOf(80.0), destinationAccount.getBalance());

        Mockito.verify(accountRepository, Mockito.times(2)).save(ArgumentMatchers.any(Account.class));
        Mockito.verify(transactionRepository, Mockito.times(1)).save(ArgumentMatchers.any(Transaction.class));
    }
}
