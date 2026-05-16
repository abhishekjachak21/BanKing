package com.bank.notification.service;

import com.bank.notification.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService{

    private final JavaMailSender mailSender;

    public void sendDepositMail(NotificationMessage message) {

        SimpleMailMessage mail =
                new SimpleMailMessage();


        mail.setTo(message.getEmail());

        mail.setSubject(
                "Cash Deposited Successfully"
        );

        mail.setText(
                "Amount ₹"
                        + message.getAmount()
                        + " deposited into account "
                        + message.getAccountNumber()
        );

        mailSender.send(mail);

        System.out.println(
                "Email sent successfully"
        );
    }
}