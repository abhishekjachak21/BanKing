package com.bank.transaction.service;

import com.bank.transaction.dto.WithdrawRequest;
import com.bank.transaction.entity.Account;
import com.bank.transaction.dto.DepositRequest;
import com.bank.transaction.dto.TransactionResponse;
import com.bank.transaction.exception.AccountNotFoundException;
import com.bank.transaction.exception.InsufficientBalanceException;
import com.bank.transaction.producer.RabbitMQProducer;
import com.bank.transaction.repository.AccountRepository;
import com.bank.transaction.util.DBConnectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bank.transaction.dto.TransactionEvent;


import java.sql.Connection;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository ;

    private final RabbitMQProducer rabbitMQProducer;

    public TransactionResponse deposit(DepositRequest request) {

        Connection connection = null;

        try {

            connection =
                    DBConnectionUtil.getConnection();

            connection.setAutoCommit(false);

            Account account =
                    accountRepository
                            .findByAccountNumber(
                                    connection,
                                    request.getAccountNumber()
                            );

            if (account == null) {
                throw new AccountNotFoundException(
                        "Account not found"
                );
            }

            Double updatedBalance =
                    account.getBalance()
                            + request.getAmount();

            accountRepository.updateBalance(
                    connection,
                    request.getAccountNumber(),
                    updatedBalance
            );

            accountRepository.insertTransaction(
                    connection,
                    request.getAccountNumber(),
                    "DEPOSIT",
                    request.getAmount()
            );

            connection.commit();

            TransactionEvent event =
                    new TransactionEvent(
                            account.getAccountNumber(),
                            account.getEmail(),
                            "DEPOSIT",
                            request.getAmount()
                    );

            rabbitMQProducer
                    .sendTransactionEvent(event);

            TransactionResponse response =
                    new TransactionResponse();

            response.setMessage(
                    "Amount deposited successfully"
            );

            response.setAccountNumber(
                    request.getAccountNumber()
            );

            response.setUpdatedBalance(
                    updatedBalance
            );

            return response;

        } catch (Exception exception) {

            try {

                if (connection != null) {
                    connection.rollback();
                }

            } catch (Exception rollbackException) {
                rollbackException.printStackTrace();
            }

            throw new RuntimeException(
                    exception.getMessage()
            );

        } finally {

            try {

                if (connection != null) {
                    connection.close();
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public TransactionResponse withdraw(
            WithdrawRequest request
    ) {

        Connection connection = null;

        try {

            connection =
                    DBConnectionUtil.getConnection();

            connection.setAutoCommit(false);

            Account account =
                    accountRepository
                            .findByAccountNumber(
                                    connection,
                                    request.getAccountNumber()
                            );

            if (account == null) {

                throw new AccountNotFoundException(
                        "Account not found"
                );
            }

            if (
                    account.getBalance()
                            < request.getAmount()
            ) {

                throw new InsufficientBalanceException(
                        "Insufficient balance"
                );
            }

            Double updatedBalance =
                    account.getBalance()
                            - request.getAmount();

            accountRepository.updateBalance(
                    connection,
                    request.getAccountNumber(),
                    updatedBalance
            );

            accountRepository.insertTransaction(
                    connection,
                    request.getAccountNumber(),
                    "WITHDRAW",
                    request.getAmount()
            );

            connection.commit();

            TransactionEvent event =
                    new TransactionEvent(
                            account.getAccountNumber(),
                            account.getEmail(),
                            "WITHDRAW",
                            request.getAmount()
                    );

            rabbitMQProducer
                    .sendTransactionEvent(event);

            TransactionResponse response =
                    new TransactionResponse();

            response.setMessage(
                    "Amount withdrawn successfully"
            );

            response.setAccountNumber(
                    request.getAccountNumber()
            );

            response.setUpdatedBalance(
                    updatedBalance
            );

            return response;

        } catch (Exception exception) {

            try {

                if (connection != null) {
                    connection.rollback();
                }

            } catch (Exception rollbackException) {
                rollbackException.printStackTrace();
            }

            throw new RuntimeException(
                    exception.getMessage()
            );

        } finally {

            try {

                if (connection != null) {
                    connection.close();
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}