package com.bank.transaction.controller;

import com.bank.transaction.api.TransactionApi;
import com.bank.transaction.dto.*;
import com.bank.transaction.service.TransactionService;
import com.bank.transaction.api.CustomerApi;
import com.bank.transaction.api.AccountApi;


import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TransactionController implements TransactionApi, CustomerApi, AccountApi {

    private final TransactionService transactionService;

    @Override
    public ResponseEntity<TransactionResponse> depositAmount(DepositRequest depositRequest) {

        return ResponseEntity.ok(
                transactionService.deposit(
                        depositRequest
                )
        );
    }

    @Override
    public ResponseEntity<TransactionResponse>
    withdrawAmount(
            WithdrawRequest withdrawRequest
    ) {

        return ResponseEntity.ok(
                transactionService.withdraw(
                        withdrawRequest
                )
        );
    }


    @Override
    public ResponseEntity<CreateCustomerResponse>
    createCustomer(
            CreateCustomerRequest createCustomerRequest
    ) {

        return ResponseEntity.ok(

                transactionService.createCustomer(
                        createCustomerRequest
                )

        );
    }


    @Override
    public ResponseEntity<CreateAccountResponse>
    createAccount(
            CreateAccountRequest createAccountRequest
    ) {

        return ResponseEntity.ok(

                transactionService.createAccount(
                        createAccountRequest
                )

        );
    }

    @Override
    public ResponseEntity<String>
    updateCustomerKyc(
            UpdateKycRequest updateKycRequest
    ) {

        return ResponseEntity.ok(
                transactionService.updateCustomerKyc(
                        updateKycRequest
                )
        );
    }

    @Override
    public ResponseEntity<FundTransferResponse>
    fundTransfer(
            FundTransferRequest fundTransferRequest
    ) {

        return ResponseEntity.ok(
                transactionService.fundTransfer(
                        fundTransferRequest
                )
        );
    }

    @Override
    public ResponseEntity<String>
    processDailyInterest() {

        return ResponseEntity.ok(
                transactionService.processDailyInterest()
        );
    }

    @Override
    public ResponseEntity<
            DailyTransactionReportResponse>
    getDailyTransactionReport() {

        return ResponseEntity.ok(
                transactionService
                        .getDailyTransactionReport()
        );
    }
}