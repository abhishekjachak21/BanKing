package com.bank.transaction.dto;

import lombok.Data;

@Data
public class FundTransferRequest {

    private String fromAccount;

    private String toAccount;

    private Double amount;
}