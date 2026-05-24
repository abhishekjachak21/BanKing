package com.bank.transaction.controller;

import com.bank.transaction.api.TransactionApi;
import com.bank.transaction.dto.DepositRequest;
import com.bank.transaction.dto.TransactionResponse;

import com.bank.transaction.dto.WithdrawRequest;
import com.bank.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService ;

    @Override
    public ResponseEntity<TransactionResponse> depositAmount( @Valid @RequestBody DepositRequest depositRequest) {

        TransactionResponse response =
                transactionService.deposit(
                        depositRequest
                );

        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<TransactionResponse> withdrawAmount(
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

}