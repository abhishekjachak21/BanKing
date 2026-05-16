package com.bank.notification.consumer;

import com.bank.notification.config.RabbitMQConfig;
import com.bank.notification.dto.NotificationMessage;
import com.bank.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final EmailService emailService;

    @RabbitListener(
            queues = RabbitMQConfig.QUEUE
    )
    public void consume(
            NotificationMessage message
    ) {

        System.out.println(
                "Message received from queue"
        );

        emailService.sendDepositMail(message);
    }
}