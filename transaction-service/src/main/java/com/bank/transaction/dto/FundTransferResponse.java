package com.bank.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FundTransferResponse {

    private String status;

    private String message;
}