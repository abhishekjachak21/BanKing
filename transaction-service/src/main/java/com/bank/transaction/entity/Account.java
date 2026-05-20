package com.bank.transaction.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private Long id;

    private String accountNumber;

    private String holderName;

    private String email;

    private String ifscCode;

    private Double balance;

}