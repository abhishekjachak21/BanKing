package com.bank.transaction.controller;

import com.bank.transaction.api.TransactionApi;
import com.bank.transaction.dto.*;
import com.bank.transaction.service.TransactionService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    // =========================
    // DEPOSIT API
    // =========================

    @Override
    public ResponseEntity<TransactionResponse>
    depositAmount(

            @Valid
            @RequestBody
            DepositRequest depositRequest

    ) {

        TransactionResponse response =
                transactionService.deposit(
                        depositRequest
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // WITHDRAW API
    // =========================

    @Override
    public ResponseEntity<TransactionResponse>
    withdrawAmount(

            @Valid
            @RequestBody
            WithdrawRequest withdrawRequest

    ) {

        TransactionResponse response =
                transactionService.withdraw(
                        withdrawRequest
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // SCENARIO 1
    // TAKES INPUT RETURNS NOTHING
    // =========================

    @PostMapping("/kyc/update")
    public ResponseEntity<String>
    updateCustomerKyc(

            @RequestBody
            UpdateKycRequest request

    ) {

        String response =
                transactionService.updateCustomerKyc(
                        request
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // SCENARIO 2
    // TAKES INPUT RETURNS OUTPUT
    // =========================

    @PostMapping("/fund-transfer")
    public ResponseEntity<FundTransferResponse>
    fundTransfer(

            @RequestBody
            FundTransferRequest request

    ) {

        FundTransferResponse response =
                transactionService.fundTransfer(
                        request
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // SCENARIO 3
    // NO INPUT RETURNS NOTHING
    // =========================

    @PostMapping("/process-interest")
    public ResponseEntity<String>
    processDailyInterest() {

        String response =
                transactionService
                        .processDailyInterest();

        return ResponseEntity.ok(response);
    }

    // =========================
    // SCENARIO 4
    // NO INPUT RETURNS OUTPUT
    // =========================

    @GetMapping("/daily-report")
    public ResponseEntity<
            DailyTransactionReportResponse
            > getDailyTransactionReport() {

        DailyTransactionReportResponse response = transactionService.getDailyTransactionReport();

        return ResponseEntity.ok(response);
    }
}