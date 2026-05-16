package com.bank.transaction.producer;

import com.bank.transaction.config.RabbitMQConfig;
import com.bank.transaction.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendDepositNotification(
            NotificationMessage message
    ) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                message
        );

        System.out.println(
                "Message Published Successfully"
        );
    }
}