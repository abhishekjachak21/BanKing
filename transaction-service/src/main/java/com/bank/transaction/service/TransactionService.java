package com.bank.transaction.service;

import com.bank.transaction.dto.*;
import com.bank.transaction.entity.Account;
import com.bank.transaction.exception.AccountNotFoundException;
import com.bank.transaction.exception.InsufficientBalanceException;
import com.bank.transaction.producer.RabbitMQProducer;
import com.bank.transaction.repository.AccountRepository;
import com.bank.transaction.util.DBConnectionUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;

    private final RabbitMQProducer rabbitMQProducer;

    // =========================================
    // DEPOSIT
    // =========================================

    public TransactionResponse deposit(DepositRequest request) {

        Connection connection = null;

        try {

            connection = DBConnectionUtil.getConnection();

            connection.setAutoCommit(false);

            Account account = accountRepository.findByAccountNumber(
                            connection,
                            request.getAccountNumber()
                    );

            if (account == null) {

                throw new AccountNotFoundException(
                        "Account not found"
                );
            }

            BigDecimal updatedBalance =
                    account.getBalance().add(new BigDecimal(request.getAmount()));

            accountRepository.updateBalance(
                    connection,
                    request.getAccountNumber(),
                    updatedBalance
            );

            accountRepository.insertTransaction(
                    connection,
                    request.getAccountNumber(),
                    "DEPOSIT",
                    new BigDecimal(request.getAmount())
            );

            connection.commit();

            TransactionResponse response = new TransactionResponse();

            response.setMessage("Amount deposited successfully");

            response.setAccountNumber(request.getAccountNumber());

            response.setUpdatedBalance(
                    updatedBalance.setScale(2, RoundingMode.HALF_UP).toString()
            );

            TransactionEvent event = new TransactionEvent(
                            account.getAccountNumber(),
                            account.getEmail(),
                            "DEPOSIT",
                            new BigDecimal(request.getAmount()),
                            new BigDecimal(response.getUpdatedBalance())
                    );

            rabbitMQProducer.sendTransactionEvent(event);

            return response;

        } catch (Exception exception) {

            rollbackConnection(connection);

            throw new RuntimeException(
                    exception.getMessage()
            );

        } finally {

            closeConnection(connection);
        }
    }

    // =========================================
    // WITHDRAW
    // =========================================

    public TransactionResponse withdraw(WithdrawRequest request) {

        Connection connection = null;

        try {

            connection = DBConnectionUtil.getConnection();

            connection.setAutoCommit(false);

            Account account =
                    accountRepository.findByAccountNumber(
                            connection,
                            request.getAccountNumber()
                    );

            if (account == null) {

                throw new AccountNotFoundException(
                        "Account not found"
                );
            }

            if (account.getBalance().compareTo(new BigDecimal(request.getAmount())) < 0) {

                throw new InsufficientBalanceException(
                        "Insufficient balance"
                );
            }

            BigDecimal updatedBalance =
                    account.getBalance().subtract(new BigDecimal(request.getAmount()));

            accountRepository.updateBalance(
                    connection,
                    request.getAccountNumber(),
                    updatedBalance
            );

            accountRepository.insertTransaction(
                    connection,
                    request.getAccountNumber(),
                    "WITHDRAW",
                    new BigDecimal(request.getAmount())
            );

            connection.commit();



            TransactionResponse response = new TransactionResponse();

            response.setMessage("Amount withdrawn successfully");

            response.setAccountNumber(request.getAccountNumber());

            response.setUpdatedBalance(
                    updatedBalance.setScale(2, RoundingMode.HALF_UP).toString()
            );

            TransactionEvent event =
                    new TransactionEvent(
                            account.getAccountNumber(),
                            account.getEmail(),
                            "WITHDRAW",
                            new BigDecimal(request.getAmount()),
                            new BigDecimal(response.getUpdatedBalance())
                    );

            rabbitMQProducer.sendTransactionEvent(event);

            return response;

        } catch (Exception exception) {

            rollbackConnection(connection);

            throw new RuntimeException(
                    exception.getMessage()
            );

        } finally {

            closeConnection(connection);
        }
    }

    // =========================================
    // SCENARIO 1
    // TAKES INPUT RETURNS NOTHING
    // =========================================

    public String updateCustomerKyc(
            UpdateKycRequest request
    ) {

        Connection connection = null;

        try {

            connection =
                    DBConnectionUtil.getConnection();

            accountRepository.updateCustomerKyc(
                    connection,
                    Math.toIntExact(
                            request.getCustomerId()
                    ),
                    request.getMobile(),
                    request.getEmail()
            );

            return "KYC Updated Successfully";

        } catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );

        } finally {

            closeConnection(connection);
        }
    }

    // =========================================
    // SCENARIO 2
    // TAKES INPUT RETURNS OUTPUT
    // =========================================

    public FundTransferResponse fundTransfer(FundTransferRequest request) {

        Connection connection = null;

        try {

            connection = DBConnectionUtil.getConnection();

            return accountRepository.fundTransfer(
                    connection,
                    request.getFromAccount(),
                    request.getToAccount(),
                    new BigDecimal(request.getAmount())
            );

        } catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );

        } finally {

            closeConnection(connection);
        }
    }

    // =========================================
    // SCENARIO 3
    // NO INPUT RETURNS NOTHING
    // =========================================

    public String processDailyInterest() {

        Connection connection = null;

        try {

            connection = DBConnectionUtil.getConnection();

            accountRepository.processDailyInterest(
                    connection
            );

            return "Daily interest processed successfully";

        } catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );

        } finally {

            closeConnection(connection);
        }
    }

    // =========================================
    // SCENARIO 4
    // NO INPUT RETURNS OUTPUT
    // =========================================

    public DailyTransactionReportResponse
    getDailyTransactionReport() {

        Connection connection = null;

        try {

            connection =
                    DBConnectionUtil.getConnection();

            return accountRepository
                    .getDailyTransactionReport(
                            connection
                    );

        } catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );

        } finally {

            closeConnection(connection);
        }
    }

    // =========================================
    // COMMON METHODS
    // =========================================

    private void rollbackConnection(
            Connection connection
    ) {

        try {

            if (connection != null) {

                connection.rollback();
            }

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    private void closeConnection(
            Connection connection
    ) {

        try {

            if (connection != null) {

                connection.close();
            }

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }
}