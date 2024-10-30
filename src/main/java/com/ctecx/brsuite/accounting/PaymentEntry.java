// File: PaymentEntry.java
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
public class PaymentEntry implements AccountingEntry {
    private final AccountChart accountsPayable;
    private final AccountChart cash;
    private final AccountChart purchaseDiscounts;
    private final BigDecimal paymentAmount;
    private final BigDecimal discountAmount;
    private final LocalDate transactionDate;
    private final String description;

    @Override
    public List<Transaction> execute() {
        List<TransactionHelper.TransactionEntry> entries = new ArrayList<>();
        entries.add(new TransactionHelper.TransactionEntry(accountsPayable, true, paymentAmount.add(discountAmount)));
        entries.add(new TransactionHelper.TransactionEntry(cash, false, paymentAmount));
        if (discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            entries.add(new TransactionHelper.TransactionEntry(purchaseDiscounts, false, discountAmount));
        }

        return TransactionHelper.createBalancedTransaction(
                transactionDate, description, "Payment", "Vendor Payment", entries);
    }

    @Override
    public void reverse() {
        // Implement reversal logic if needed
    }
}
