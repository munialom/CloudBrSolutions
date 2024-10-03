package com.ctecx.brsuite.transactions;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/sale")
    public ResponseEntity<?> processSaleTransaction(@RequestBody SaleTransactionDTO saleTransactionDTO) {
        try {
            List<Transaction> result = transactionService.processSaleTransaction(saleTransactionDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace
            return ResponseEntity.badRequest().body("Error processing transaction: " + e.getMessage());
        }
    }
}