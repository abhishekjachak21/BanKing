package com.bank.transaction.producer;

import com.bank.transaction.config.RabbitMQConfig;
import com.bank.transaction.dto.AccountCreatedEvent;
import com.bank.transaction.dto.TransactionEvent;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendTransactionEvent(TransactionEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );

        System.out.println("Sent to RabbitMQ");

    }


    public void sendAccountCreatedEvent(
            AccountCreatedEvent event
    ) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ACCOUNT_ROUTING_KEY,
                event
        );

        System.out.println(
                "Account Created Event Published"
        );
    }

}