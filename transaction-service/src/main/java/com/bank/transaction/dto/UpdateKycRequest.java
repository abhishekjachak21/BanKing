package com.bank.transaction.dto;

import lombok.Data;

@Data
public class UpdateKycRequest {

    private Long customerId;

    private String mobile;

    private String email;
}