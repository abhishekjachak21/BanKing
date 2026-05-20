package com.bank.transaction.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepositRequest {

    private String accountNumber;

    private Double amount;

}

//IFSC no