// File: AccountingEntryFactory.java
package com.ctecx.brsuite.accounting;

import com.ctecx.brsuite.chartofaccounts.AccountChart;
import com.ctecx.brsuite.chartofaccounts.AccountChartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class AccountingEntryFactory {
    private final AccountChartService accountChartService;

    public AccountingEntry createEntry(TransactionDetails details) {
        switch (details.getType()) {
            case PURCHASE:
                return createPurchaseEntry(details);
            case SALE:
                return createSaleEntry(details);
            case PAYMENT:
                return createPaymentEntry(details);
            case RECEIPT:
                return createReceiptEntry(details);
            default:
                throw new IllegalArgumentException("Unsupported transaction type");
        }
    }

    private AccountingEntry createPurchaseEntry(TransactionDetails details) {
        AccountChart inventory = accountChartService.getAccountByName("Inventory");
        AccountChart inputVAT = accountChartService.getAccountByName("Input VAT");
        AccountChart accountsPayable = accountChartService.getAccountByName("Accounts Payable");
        AccountChart cash = accountChartService.getAccountByName("Cash");

        BigDecimal discountedAmount = details.getDiscountStrategy().applyDiscount(details.getPrincipalAmount());
        BigDecimal taxAmount = details.getTaxStrategy().calculateTax(discountedAmount);

        return new PurchaseEntry(inventory, inputVAT,
                details.getPaymentType() == PaymentType.CASH ? cash : accountsPayable,
                discountedAmount, taxAmount, details.getTransactionDate(), details.getDescription());
    }

    private AccountingEntry createSaleEntry(TransactionDetails details) {
        AccountChart accountsReceivable = accountChartService.getAccountByName("Accounts Receivable");
        AccountChart salesRevenue = accountChartService.getAccountByName("Sales Revenue");
        AccountChart salesTaxPayable = accountChartService.getAccountByName("Sales Tax Payable");
        AccountChart inventory = accountChartService.getAccountByName("Inventory");
        AccountChart costOfGoodsSold = accountChartService.getAccountByName("Cost of Goods Sold");
        AccountChart cash = accountChartService.getAccountByName("Cash");

        BigDecimal discountedAmount = details.getDiscountStrategy().applyDiscount(details.getPrincipalAmount());
        BigDecimal taxAmount = details.getTaxStrategy().calculateTax(discountedAmount);
        BigDecimal costAmount = details.getPaidAmount(); // Assuming paidAmount represents the cost in this context

        return new SaleEntry(
                details.getPaymentType() == PaymentType.CASH ? cash : accountsReceivable,
                salesRevenue, salesTaxPayable, inventory, costOfGoodsSold,
                discountedAmount, taxAmount, costAmount,
                details.getTransactionDate(), details.getDescription());
    }

    private AccountingEntry createPaymentEntry(TransactionDetails details) {
        AccountChart accountsPayable = accountChartService.getAccountByName("Accounts Payable");
        AccountChart cash = accountChartService.getAccountByName("Cash");
        AccountChart purchaseDiscounts = accountChartService.getAccountByName("Purchase Discounts");

        BigDecimal discountAmount = details.getPrincipalAmount().subtract(
                details.getDiscountStrategy().applyDiscount(details.getPrincipalAmount()));

        return new PaymentEntry(accountsPayable, cash, purchaseDiscounts,
                details.getPaidAmount(), discountAmount,
                details.getTransactionDate(), details.getDescription());
    }

    private AccountingEntry createReceiptEntry(TransactionDetails details) {
        AccountChart cash = accountChartService.getAccountByName("Cash");
        AccountChart accountsReceivable = accountChartService.getAccountByName("Accounts Receivable");
        AccountChart salesDiscounts = accountChartService.getAccountByName("Sales Discounts");

        BigDecimal discountAmount = details.getPrincipalAmount().subtract(
                details.getDiscountStrategy().applyDiscount(details.getPrincipalAmount()));

        return new ReceiptEntry(cash, accountsReceivable, salesDiscounts,
                details.getPaidAmount(), discountAmount,
                details.getTransactionDate(), details.getDescription());
    }
}