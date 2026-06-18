package com.bank.notification.consumer;

import com.bank.notification.dto.NotificationResponseEvent;
import com.bank.notification.dto.TransactionEvent;
import com.bank.notification.producer.NotificationResponseProducer;
import com.bank.notification.service.EmailService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionConsumer {

    private final EmailService emailService;

    private final NotificationResponseProducer responseProducer;


    @RabbitListener(queues = "transaction.queue")
    public void consumeTransaction(TransactionEvent event) {

        log.info("Received transaction event: {}", event);

        try {
            String subject = "Transaction Successful";

            String body = """
                    Dear Customer,
                    
                    Your transaction was successful.
                    
                    Account Number: %s
                    Transaction Type: %s
                    Amount: %s
                    Total Balance: %s
                    
                    Thank You.
                    """
                    .formatted(
                            event.getAccountNumber(),
                            event.getTransactionType(),
                            event.getAmount(),
                            event.getTotalBalance()
                    );

//            if(true){
//                throw new RuntimeException(
//                        "Testing DLQ"
//                );
//            }

            if (event.getEmail().contains("fail")) {
            throw new RuntimeException(
                    "Email sending failed"
            );
        }

            emailService.sendEmail(
                    event.getEmail(),
                    subject,
                    body
            );

            NotificationResponseEvent response =
                    new NotificationResponseEvent(
                            event.getAccountNumber(),
                            event.getEmail(),
                            "SUCCESS",
                            "Email Sent Successfully"
                    );

            responseProducer.publishResponse(
                    response
            );

        } catch (Exception ex) {

            NotificationResponseEvent response =
                    new NotificationResponseEvent(
                            event.getAccountNumber(),
                            event.getEmail(),
                            "FAILED",
                            ex.getMessage()
                    );

            responseProducer.publishResponse(
                    response
            );

            log.error(
                    "Email failed for {}",
                    event.getEmail()
            );

            throw new RuntimeException(ex);

        }


        log.info(
                "Email sent to: {}",
                event.getEmail()
        );
    }
}