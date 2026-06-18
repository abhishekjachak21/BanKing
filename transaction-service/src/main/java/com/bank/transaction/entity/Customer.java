package com.bank.transaction.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {

    private Long customerId;
    private String customerName;
    private String mobile;
    private String email;
    private String panNumber;


}