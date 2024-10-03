// File: SaleEntry.java
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
public class SaleEntry implements AccountingEntry {
    private final AccountChart revenueAccount; // This can be either cash or accounts receivable
    private final AccountChart salesRevenue;
    private final AccountChart salesTaxPayable;
    private final AccountChart inventory;
    private final AccountChart costOfGoodsSold;
    private final BigDecimal saleAmount;
    private final BigDecimal taxAmount;
    private final BigDecimal costAmount;
    private final LocalDate transactionDate;
    private final String description;

    @Override
    public List<Transaction> execute() {
        List<Transaction> allTransactions = new ArrayList<>();

        List<TransactionHelper.TransactionEntry> saleEntries = new ArrayList<>();
        saleEntries.add(new TransactionHelper.TransactionEntry(revenueAccount, true, saleAmount.add(taxAmount)));
        saleEntries.add(new TransactionHelper.TransactionEntry(salesRevenue, false, saleAmount));
        saleEntries.add(new TransactionHelper.TransactionEntry(salesTaxPayable, false, taxAmount));

        allTransactions.addAll(TransactionHelper.createBalancedTransaction(
                transactionDate, description, "Invoice", "Sale", saleEntries));

        List<TransactionHelper.TransactionEntry> cogEntries = new ArrayList<>();
        cogEntries.add(new TransactionHelper.TransactionEntry(costOfGoodsSold, true, costAmount));
        cogEntries.add(new TransactionHelper.TransactionEntry(inventory, false, costAmount));

        allTransactions.addAll(TransactionHelper.createBalancedTransaction(
                transactionDate, "Cost of Goods Sold", "Journal", "COGS", cogEntries));

        return allTransactions;
    }

    @Override
    public void reverse() {
        // Implement reversal logic if needed
    }
}