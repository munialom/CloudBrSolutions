package com.ctecx.brsuite.accounting;

import java.math.BigDecimal;

public interface TaxStrategy {
    BigDecimal calculateTax(BigDecimal amount);
}