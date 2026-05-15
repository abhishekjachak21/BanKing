package com.bank.transaction.service;

import com.bank.transaction.dto.DepositRequest;
import com.bank.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements  TransactionService{

    private final TransactionRepository transactionRepository;

    @Override
    public void deposit(DepositRequest depositRequest) {

        transactionRepository.depositAmount(depositRequest);

    }
}
