package com.bank.transaction.consumer;

import com.bank.transaction.config.RabbitMQConfig;
import com.bank.transaction.dto.NotificationResponseEvent;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationResponseConsumer {

    @RabbitListener(
            queues =
                    RabbitMQConfig.RESPONSE_QUEUE
    )
    public void consumeResponse(
            NotificationResponseEvent event
    ) {

        log.info(
                "Response Received : {}",
                event
        );

        if ("SUCCESS".equals(
                event.getStatus()
        )) {

            log.info(
                    "Email Sent Successfully"
            );

        } else {

            log.error(
                    "Email Failed : {}",
                    event.getMessage()
            );
        }
    }
}