package com.ctecx.brsuite.util;

import com.ctecx.brsuite.chartofaccounts.AccountChart;
import com.ctecx.brsuite.transactions.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionHelper {
    public static class TransactionEntry {
        private AccountChart account;
        private boolean isDebit;
        private BigDecimal amount;

        public TransactionEntry(AccountChart account, boolean isDebit, BigDecimal amount) {
            this.account = account;
            this.isDebit = isDebit;
            this.amount = amount;
        }

        // Getters and setters
        public AccountChart getAccount() { return account; }
        public boolean isDebit() { return isDebit; }
        public BigDecimal getAmount() { return amount; }
    }

    public static List<Transaction> createBalancedTransaction(LocalDate transactionDate, String description,
                                                              String paymentMode, String module,
                                                              List<TransactionEntry> entries) {
        List<Transaction> transactions = new ArrayList<>();
        BigDecimal totalDebits = BigDecimal.ZERO;
        BigDecimal totalCredits = BigDecimal.ZERO;
        String serialNumber = generateSerialNumber();

        for (TransactionEntry entry : entries) {
            Transaction transaction = new Transaction();
            transaction.setTransactionDate(transactionDate);
            transaction.setDescription(description);
            transaction.setPaymentMode(paymentMode);
            transaction.setModule(module);
            transaction.setAccountChart(entry.account);
            transaction.setAccountName(entry.account.getName());
            transaction.setSerialNumber(serialNumber);
            applyTransaction(transaction, entry.isDebit, entry.amount);
            transactions.add(transaction);

            if (entry.isDebit) {
                totalDebits = totalDebits.add(entry.amount);
            } else {
                totalCredits = totalCredits.add(entry.amount);
            }
        }

        // Check if the transaction is balanced
        if (totalDebits.subtract(totalCredits).abs().compareTo(new BigDecimal("0.001")) > 0) {
            throw new IllegalArgumentException("Transaction is not balanced. Total debits: " + totalDebits + ", Total credits: " + totalCredits);
        }

        return transactions;
    }

    private static void applyTransaction(Transaction transaction, boolean isDebit, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }

        if (isDebit) {
            transaction.setDebit(amount);
            transaction.setCredit(BigDecimal.ZERO);
        } else {
            transaction.setDebit(BigDecimal.ZERO);
            transaction.setCredit(amount);
        }

        transaction.setAmount(amount);

        // Determine the effect on the account balance based on the account group
        switch (transaction.getAccountChart().getAccountGroupEnum()) {
            case CURRENT_ASSETS:
            case FIXED_ASSETS:
            case LONG_TERM_INVESTMENTS:
            case OTHER_ASSETS:
            case OPERATING_EXPENSES:
            case NON_OPERATING_EXPENSES:
            case COST_OF_GOODS_SOLD:
                transaction.setStatus(isDebit ? "Debit" : "Credit");
                break;
            case CURRENT_LIABILITIES:
            case LONG_TERM_LIABILITIES:
            case SHARE_CAPITAL:
            case RETAINED_EARNINGS:
            case OPERATING_REVENUE:
            case NON_OPERATING_REVENUE:
                transaction.setStatus(isDebit ? "Credit" : "Debit");
                break;
            default:
                throw new IllegalStateException("Unknown account group: " + transaction.getAccountChart().getAccountGroupEnum());
        }

        transaction.setTransaction_type(isDebit ? "Debit" : "Credit");
    }

    private static String generateSerialNumber() {
        return UUID.randomUUID().toString();
    }
}