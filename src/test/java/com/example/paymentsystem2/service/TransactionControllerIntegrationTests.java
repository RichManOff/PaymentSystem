package com.example.paymentsystem2.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.paymentsystem2.model.Account;
import com.example.paymentsystem2.repository.AccountRepository;
import com.example.paymentsystem2.repository.TransactionRepository;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testTransferFunds_SuccessfulTransaction() throws Exception {
        Account sourceAccount = new Account("testUser1", "testPassword1", BigDecimal.valueOf(100.0));
        Account destinationAccount = new Account("testUser2", "testPassword2", BigDecimal.valueOf(50.0));

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        String requestBody = "{\"sourceAccountId\": " + sourceAccount.getId() +
                ", \"destinationAccountId\": " + destinationAccount.getId() +
                ", \"amount\": 30}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Account updatedSourceAccount = accountRepository.findById(sourceAccount.getId()).get();
        Account updatedDestinationAccount = accountRepository.findById(destinationAccount.getId()).get();

        assertEquals(BigDecimal.valueOf(70.0), updatedSourceAccount.getBalance());
        assertEquals(BigDecimal.valueOf(80.0), updatedDestinationAccount.getBalance());
    }
}
