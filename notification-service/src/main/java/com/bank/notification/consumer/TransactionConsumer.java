package com.bank.notification.consumer;

import com.bank.notification.dto.TransactionEvent;
import com.bank.notification.service.EmailService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionConsumer {

    private final EmailService
            emailService;

    @RabbitListener(
            queues = "transaction.queue"
    )
    public void consumeTransaction(
            TransactionEvent event
    ) {

        log.info(
                "Received transaction event: {}",
                event
        );

        String subject =
                "Transaction Successful";

        String body = """
                Dear Customer,

                Your transaction was successful.

                Account Number: %s
                Transaction Type: %s
                Amount: %s

                Thank You.
                """
                .formatted(
                        event.getAccountNumber(),
                        event.getTransactionType(),
                        event.getAmount()
                );

        emailService.sendEmail(
                event.getEmail(),
                subject,
                body
        );

        log.info(
                "Email sent to: {}",
                event.getEmail()
        );
    }
}