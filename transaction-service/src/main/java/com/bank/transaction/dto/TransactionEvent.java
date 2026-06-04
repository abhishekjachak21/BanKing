package com.bank.transaction.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEvent {

    private String accountNumber;

    private String email;

    private String transactionType;

    private BigDecimal amount;

    private BigDecimal totalBalance;

}