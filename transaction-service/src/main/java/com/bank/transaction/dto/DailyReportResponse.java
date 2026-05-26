package com.bank.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyReportResponse {

    private Integer totalTransactions;

    private Double totalAmount;
}