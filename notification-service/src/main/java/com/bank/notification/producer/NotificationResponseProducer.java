package com.bank.notification.producer;

import com.bank.notification.dto.NotificationResponseEvent;
import com.bank.notification.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationResponseProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishResponse(
            NotificationResponseEvent event
    ) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.RESPONSE_ROUTING_KEY,
                event
        );
    }
}