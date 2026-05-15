package com.bank.transaction.controller;

import com.bank.transaction.dto.DepositRequest;
import com.bank.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public String deposit(@RequestBody DepositRequest depositRequest){

        transactionService.deposit(depositRequest);

        return "Amount depositd success";

    }

}
