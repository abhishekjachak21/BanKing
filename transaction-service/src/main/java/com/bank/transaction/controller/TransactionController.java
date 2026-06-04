package com.bank.transaction.controller;

import com.bank.transaction.api.TransactionApi;
import com.bank.transaction.dto.DailyTransactionReportResponse;
import com.bank.transaction.dto.DepositRequest;
import com.bank.transaction.dto.FundTransferRequest;
import com.bank.transaction.dto.FundTransferResponse;
import com.bank.transaction.dto.TransactionResponse;
import com.bank.transaction.dto.UpdateKycRequest;
import com.bank.transaction.dto.WithdrawRequest;
import com.bank.transaction.service.TransactionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {

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