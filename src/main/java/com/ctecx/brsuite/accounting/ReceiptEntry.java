// File: ReceiptEntry.java
package com.ctecx.brsuite.accounting;

import com.ctecx.brsuite.chartofaccounts.AccountChart;
import com.ctecx.brsuite.transactions.Transaction;
import com.ctecx.brsuite.util.TransactionHelper;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ReceiptEntry implements AccountingEntry {
    private final AccountChart cash;
    private final AccountChart accountsReceivable;
    private final AccountChart salesDiscounts;
    private final BigDecimal receiptAmount;
    private final BigDecimal discountAmount;
    private final LocalDate transactionDate;
    private final String description;

    @Override
    public List<Transaction> execute() {
        List<TransactionHelper.TransactionEntry> entries = new ArrayList<>();
        entries.add(new TransactionHelper.TransactionEntry(cash, true, receiptAmount));
        entries.add(new TransactionHelper.TransactionEntry(accountsReceivable, false, receiptAmount.add(discountAmount)));
        if (discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            entries.add(new TransactionHelper.TransactionEntry(salesDiscounts, true, discountAmount));
        }

        return TransactionHelper.createBalancedTransaction(
                transactionDate, description, "Receipt", "Customer Payment", entries);
    }

    @Override
    public void reverse() {
        // Implement reversal logic if needed
    }
}