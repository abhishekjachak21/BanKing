package com.bank.transaction.service;

import com.bank.transaction.dto.DepositRequest;

public interface TransactionService {

    void deposit(DepositRequest depositRequest);

}
