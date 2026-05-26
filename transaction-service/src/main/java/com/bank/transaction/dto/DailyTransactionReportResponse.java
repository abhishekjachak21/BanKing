package com.bank.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyTransactionReportResponse {

    private Integer totalTransactions;

    private Double totalAmount;
}