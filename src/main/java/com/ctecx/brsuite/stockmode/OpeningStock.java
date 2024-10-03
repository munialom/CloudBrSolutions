package com.ctecx.brsuite.stockmode;

import lombok.Data;

@Data
public class OpeningStock {
    private String productCode;
    private String productName;
    private double buyPrice;
    private double salePrice;
    private int qty;
}
