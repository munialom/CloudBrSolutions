package com.ctecx.brsuite.accounting;

import com.ctecx.brsuite.transactions.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class SimpleAccountingSystem implements AccountingSystem {
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void recordEntry(AccountingEntry entry) {
        transactions.addAll(entry.execute());
    }

    @Override
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}