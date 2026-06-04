package com.bank.transaction.consumer;

import com.bank.transaction.config.RabbitMQConfig;
import com.bank.transaction.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyInterestConsumer {

    private final TransactionService transactionService;

    @RabbitListener(queues = RabbitMQConfig.DAILY_INTEREST_QUEUE)
    public void consume() {

        log.info(
                "Daily Interest Event Received"
        );

        String response =
                transactionService
                        .processDailyInterest();

        log.info(
                "Interest Processing Result : {}",
                response
        );
    }
}