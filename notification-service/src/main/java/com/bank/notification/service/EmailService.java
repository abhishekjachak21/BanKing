package com.bank.notification.service;

import com.bank.notification.dto.AccountCreatedEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(
            String to,
            String subject,
            String body
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);

        message.setSubject(subject);

        message.setText(body);

        mailSender.send(message);
    }

    public void sendAccountCreatedEmail(
            AccountCreatedEvent event
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(
                event.getEmail()
        );

        message.setSubject(
                "Account Created Successfully"
        );

        message.setText(
                "Dear " + event.getCustomerName()
                        + "\n\nYour account has been created successfully."
                        + "\n\nAccount Number : "
                        + event.getAccountNumber()
                        + "\nAccount Type : "
                        + event.getAccountType()
                        + "\nIFSC Code : "
                        + event.getIfscCode()
                        + "\nInitial Balance : ₹"
                        + event.getInitialBalance()
                        + "\n\nThank you for choosing our bank."

        );

        mailSender.send(message);
    }

}