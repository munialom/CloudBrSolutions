package com.ctecx.brsuite.purchases;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseStock {
    private String productCode;
    private String productName;
    private double unitcost;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal subtotal;
    private int qty;
}
