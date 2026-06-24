package com.bank.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreatedEvent {

    private String customerName;

    private String email;

    private String accountNumber;

    private String accountType;

    private String ifscCode;

    private BigDecimal initialBalance;
}