package com.example.paymentsystem2.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateAccount() throws Exception {
        String requestBody = "{\"username\": \"testUser\", \"password\": \"testPassword\", \"initialBalance\": 100}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
