package com.ctecx.brsuite.stockmode;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Stock {
    private String productCode;
    private String productName;
    private int qty;
    private double cost;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal netTax;
    private BigDecimal subtotal;
}
