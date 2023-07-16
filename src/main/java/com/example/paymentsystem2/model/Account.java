package com.example.paymentsystem2.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account number is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @DecimalMin(value = "0.0", inclusive = false, message = "Initial balance must be greater than 0")
    private BigDecimal balance;

    public Account() {
    }

    public Account(String username, String password, BigDecimal balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
    }
}
