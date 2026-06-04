package com.bank.transaction.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEvent {

    private String accountNumber;

    private String email;

    private String transactionType;

    private Double amount;



}