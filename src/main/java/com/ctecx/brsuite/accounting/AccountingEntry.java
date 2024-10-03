package com.ctecx.brsuite.accounting;

import com.ctecx.brsuite.transactions.Transaction;

import java.util.List;

public interface AccountingEntry {
    List<Transaction> execute();
    void reverse();
}
