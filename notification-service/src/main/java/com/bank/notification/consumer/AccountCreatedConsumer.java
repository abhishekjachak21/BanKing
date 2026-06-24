package com.bank.notification.consumer;

import com.bank.notification.config.RabbitMQConfig;
import com.bank.notification.dto.AccountCreatedEvent;
import com.bank.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountCreatedConsumer {

    private final EmailService emailService;

    @RabbitListener(
            queues = RabbitMQConfig.ACCOUNT_QUEUE
    )
    public void consume(
            AccountCreatedEvent event
    ) {

        System.out.println(
                "Received Account Created Event"
        );

        emailService.sendAccountCreatedEmail(
                event
        );
    }
}