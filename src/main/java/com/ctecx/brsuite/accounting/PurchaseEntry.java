// File: PurchaseEntry.java
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
public class PurchaseEntry implements AccountingEntry {
    private final AccountChart inventory;
    private final AccountChart inputVAT;
    private final AccountChart counterAccount; // This can be either cash or accounts payable
    private final BigDecimal inventoryAmount;
    private final BigDecimal taxAmount;
    private final LocalDate transactionDate;
    private final String description;

    @Override
    public List<Transaction> execute() {
        List<TransactionHelper.TransactionEntry> entries = new ArrayList<>();
        entries.add(new TransactionHelper.TransactionEntry(inventory, true, inventoryAmount));
        entries.add(new TransactionHelper.TransactionEntry(inputVAT, true, taxAmount));
        entries.add(new TransactionHelper.TransactionEntry(counterAccount, false, inventoryAmount.add(taxAmount)));

        return TransactionHelper.createBalancedTransaction(
                transactionDate, description, "Invoice", "Purchase", entries);
    }

    @Override
    public void reverse() {
        // Implement reversal logic if needed
    }
}
