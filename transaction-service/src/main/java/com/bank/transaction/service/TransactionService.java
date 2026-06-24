package com.bank.transaction.service;

import com.bank.transaction.dto.*;
import com.bank.transaction.entity.Account;
import com.bank.transaction.exception.AccountNotFoundException;
import com.bank.transaction.exception.InsufficientBalanceException;
import com.bank.transaction.producer.RabbitMQProducer;
import com.bank.transaction.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Transactional
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;

    private final RabbitMQProducer rabbitMQProducer;

    // =========================================
    // DEPOSIT
    // =========================================

    public TransactionResponse deposit(DepositRequest request) {

        Account account =
                accountRepository.findByAccountNumber(
                        request.getAccountNumber()
                );

        if (account == null) {

            throw new AccountNotFoundException(
                    "Account not found"
            );
        }

        BigDecimal updatedBalance =
                account.getBalance()
                        .add(new BigDecimal(request.getAmount()));

        accountRepository.updateBalance(
                request.getAccountNumber(),
                updatedBalance
        );

//        if (true) {
//            throw new RuntimeException("Testing");
//        }


        accountRepository.insertTransaction(
                request.getAccountNumber(),
                "DEPOSIT",
                new BigDecimal(request.getAmount())
        );

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
                        .setScale(2, RoundingMode.HALF_UP)
                        .toString()
        );

        TransactionEvent event =
                new TransactionEvent(
                        account.getAccountNumber(),
                        account.getEmail(),
                        "DEPOSIT",
                        new BigDecimal(request.getAmount()),
                        updatedBalance
                );

        System.out.println(event);

        rabbitMQProducer.sendTransactionEvent(
                event
        );

        System.out.println("Message Published Successfully");

        return response;
    }

    // =========================================
    // WITHDRAW
    // =========================================

    public TransactionResponse withdraw(WithdrawRequest request) {

        Account account =
                accountRepository.findByAccountNumber(
                        request.getAccountNumber()
                );

        if (account == null) {

            throw new AccountNotFoundException(
                    "Account not found"
            );
        }

        if (account.getBalance().compareTo(
                new BigDecimal(request.getAmount())
        ) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance"
            );
        }

        BigDecimal updatedBalance =
                account.getBalance()
                        .subtract(
                                new BigDecimal(
                                        request.getAmount()
                                )
                        );

        accountRepository.updateBalance(
                request.getAccountNumber(),
                updatedBalance
        );

        accountRepository.insertTransaction(
                request.getAccountNumber(),
                "WITHDRAW",
                new BigDecimal(request.getAmount())
        );

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
                        .setScale(2, RoundingMode.HALF_UP)
                        .toString()
        );

        TransactionEvent event =
                new TransactionEvent(
                        account.getAccountNumber(),
                        account.getEmail(),
                        "WITHDRAW",
                        new BigDecimal(request.getAmount()),
                        updatedBalance
                );

        rabbitMQProducer.sendTransactionEvent(
                event
        );

        return response;
    }

    // =========================================
    // SCENARIO 1
    // TAKES INPUT RETURNS NOTHING
    // =========================================

    public String updateCustomerKyc(UpdateKycRequest request) {

        accountRepository.updateCustomerKyc(

                Math.toIntExact(
                        request.getCustomerId()
                ),

                request.getMobile(),

                request.getEmail()

        );

        return "KYC Updated Successfully";

    }

    // =========================================
    // SCENARIO 2
    // TAKES INPUT RETURNS OUTPUT
    // =========================================

    public FundTransferResponse fundTransfer(FundTransferRequest request) {

        return accountRepository.fundTransfer(

                request.getFromAccount(),

                request.getToAccount(),

                new BigDecimal(
                        request.getAmount()
                )

        );

    }

    // =========================================
    // SCENARIO 3
    // NO INPUT RETURNS NOTHING
    // =========================================

    public String processDailyInterest() {

        accountRepository.processDailyInterest();

        return "Daily interest processed successfully";

    }

    // =========================================
    // SCENARIO 4
    // NO INPUT RETURNS OUTPUT
    // =========================================

    public DailyTransactionReportResponse getDailyTransactionReport() {

        return accountRepository
                .getDailyTransactionReport();

    }

//    =========================================



    public CreateCustomerResponse createCustomer(CreateCustomerRequest request) {


        if (accountRepository.existsByEmail(
                request.getEmail()
        )) {

            throw new RuntimeException(
                    "Email already exists"
            );

        }

        if (accountRepository.existsByMobile(
                request.getMobile()
        )) {

            throw new RuntimeException(
                    "Mobile number already exists"
            );

        }

        if (accountRepository.existsByPanNumber(
                request.getPanNumber()
        )) {

            throw new RuntimeException(
                    "PAN number already exists"
            );

        }

        Integer customerId =
                accountRepository.createCustomer(

                        request.getCustomerName(),

                        request.getMobile(),

                        request.getEmail(),

                        request.getPanNumber()

                );


        CreateCustomerResponse response = new CreateCustomerResponse();

        response.setCustomerId(customerId);

        response.setMessage("Customer created successfully");

        return response;

    }



    public CreateAccountResponse createAccount(CreateAccountRequest request) {

        if (!accountRepository.customerExists(
                request.getCustomerId()
        )) {

            throw new RuntimeException(
                    "Customer does not exist"
            );

        }

        if (accountRepository.existsByCustomerId(
                request.getCustomerId()
        )) {

            throw new RuntimeException(
                    "Customer already has an account"
            );

        }

        BigDecimal initialBalance = new BigDecimal(request.getInitialBalance());

        if (initialBalance.compareTo(
                BigDecimal.valueOf(3000)
        ) < 0) {

            throw new RuntimeException(
                    "Minimum balance should be 3000"
            );

        }


        String accountNumber =
                accountRepository.createAccount(

                        request.getCustomerId(),

                        request.getAccountType(),

                        new BigDecimal(
                                request.getInitialBalance()
                        )

                );

        AccountCreatedEvent event =
                accountRepository.getAccountCreatedEventData(
                        request.getCustomerId(),
                        accountNumber
                );

        event.setInitialBalance(
                new BigDecimal(
                        request.getInitialBalance()
                )
        );

        rabbitMQProducer.sendAccountCreatedEvent(
                event
        );

        CreateAccountResponse response =
                new CreateAccountResponse();

        response.setAccountNumber(
                accountNumber
        );

        response.setIfscCode(
                "SBIN0001234"
        );

        response.setMessage(
                "Account created successfully"
        );

        return response;

    }

}