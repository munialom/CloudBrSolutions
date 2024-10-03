package com.ctecx.brsuite.accounting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class TransactionDetails {
    private final TransactionType type;
    private final PaymentType paymentType;
    private final BigDecimal principalAmount;
    private final BigDecimal paidAmount;
    private final LocalDate transactionDate;
    private final String description;
    private final DiscountStrategy discountStrategy;
    private final TaxStrategy taxStrategy;
}