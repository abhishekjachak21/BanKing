package com.bank.transaction.service;

import com.bank.transaction.dto.DepositRequest;
import com.bank.transaction.dto.NotificationMessage;
import com.bank.transaction.producer.TransactionProducer;
import com.bank.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    private final TransactionProducer producer;

    @Override
    public void deposit(DepositRequest request) {

        repository.depositAmount(request);

        String email = repository.getEmailByAccountNumber(request.getAccountNumber());

        NotificationMessage message =
                new NotificationMessage(
                        email,
                        request.getAccountNumber(),
                        request.getAmount()
                );

        producer.sendDepositNotification(message);

    }
}