package com.bank.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {

    private String accountNumber;

    private String email;

    private String transactionType;

    private Double amount;
}