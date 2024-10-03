package com.ctecx.brsuite.accounting;

import com.ctecx.brsuite.transactions.Transaction;

import java.util.List;

public interface AccountingSystem {
    void recordEntry(AccountingEntry entry);
    List<Transaction> getTransactions();
}