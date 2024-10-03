package com.ctecx.brsuite.accounting;

import com.ctecx.brsuite.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounting")
public class AccountingController {

    private final AccountingEntryFactory accountingEntryFactory;
    private final SimpleAccountingSystem accountingSystem;

    @Autowired
    public AccountingController(AccountingEntryFactory accountingEntryFactory, SimpleAccountingSystem accountingSystem) {
        this.accountingEntryFactory = accountingEntryFactory;
        this.accountingSystem = accountingSystem;
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> createPurchaseEntry(@RequestBody TransactionDetails details) {
        return createAndSaveEntry(details, TransactionType.PURCHASE);
    }

    @PostMapping("/sale")
    public ResponseEntity<String> createSaleEntry(@RequestBody TransactionDetails details) {
        return createAndSaveEntry(details, TransactionType.SALE);
    }

    @PostMapping("/payment")
    public ResponseEntity<String> createPaymentEntry(@RequestBody TransactionDetails details) {
        return createAndSaveEntry(details, TransactionType.PAYMENT);
    }

    @PostMapping("/receipt")
    public ResponseEntity<String> createReceiptEntry(@RequestBody TransactionDetails details) {
        return createAndSaveEntry(details, TransactionType.RECEIPT);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = accountingSystem.getTransactions();
        return ResponseEntity.ok(transactions);
    }

    private ResponseEntity<String> createAndSaveEntry(TransactionDetails details, TransactionType expectedType) {
        if (details.getType() != expectedType) {
            return ResponseEntity.badRequest().body("Transaction type mismatch");
        }

        try {
            AccountingEntry entry = accountingEntryFactory.createEntry(details);
            accountingSystem.recordEntry(entry);
            return ResponseEntity.ok("Entry created and recorded successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while processing the entry");
        }
    }
}