package com.ctecx.brsuite.purchases;

import lombok.Data;

@Data
public class StoreOpeningStock {
    private String productCode;
    private String productName;
    private double buyPrice;
    private double salePrice;
    private int qty;
}
