package com.example.paymentsystem2.service;

import java.io.FileWriter;
import java.io.IOException;

import com.example.paymentsystem2.model.Account;
import com.example.paymentsystem2.model.Transaction;
import com.example.paymentsystem2.repository.AccountRepository;
import com.example.paymentsystem2.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private static final String TRANSACTION_LOG_FILE = "transaction_history.log";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Autowired
    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction transferFunds(Long sourceAccountId, Long destinationAccountId, BigDecimal amount) {
        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        Account destinationAccount = accountRepository.findById(destinationAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));
        if (sourceAccount.equals(destinationAccount)) {
            throw new IllegalArgumentException("Cannot send funds to your own account");
        }

        BigDecimal sourceBalance = sourceAccount.getBalance();
        if (sourceBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance in the source account");
        }

        BigDecimal newSourceBalance = sourceBalance.subtract(amount);
        BigDecimal destinationBalance = destinationAccount.getBalance().add(amount);
        BigDecimal minimumBalance = new BigDecimal("10.00");
        BigDecimal maximumAllowedAmount = newSourceBalance.subtract(minimumBalance);

        if (amount.compareTo(maximumAllowedAmount) > 0) {
            throw new IllegalArgumentException("You can only send up to " + maximumAllowedAmount + " while keeping a minimum balance of 10.");
        }

        sourceAccount.setBalance(newSourceBalance);
        destinationAccount.setBalance(destinationBalance);

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        return transactionHistory(sourceAccount, destinationAccount, amount);
    }

    public Transaction transactionHistory(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setSender(sourceAccount);
        transaction.setReceiver(destinationAccount);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
        logTransactionToFile(transaction);

        return transaction;
    }

    private void logTransactionToFile(Transaction transaction) {
        try (FileWriter fileWriter = new FileWriter(TRANSACTION_LOG_FILE, true)) {
            String logMessage = String.format(
                    "%s - Transaction History - Sender: %s, Receiver: %s, Amount: %s%n",
                    DATE_TIME_FORMATTER.format(transaction.getTimestamp()),
                    transaction.getSender().getId(),
                    transaction.getReceiver().getId(),
                    transaction.getAmount()
            );
            fileWriter.write(logMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
