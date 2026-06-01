package com.bank.transaction.producer;

import com.bank.transaction.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyInterestProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishDailyInterestJob() {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.DAILY_INTEREST_ROUTING_KEY,
                "PROCESS_DAILY_INTEREST"
        );
    }
}