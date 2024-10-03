package com.ctecx.brsuite.purchases;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductDetail {
    private double productCost;
    private double productSalePrice;
    private Integer stockIn;
    private Integer stockOut;
    private String productName;
    private BigDecimal netTax;
    private BigDecimal subtotal;

    public ProductDetail(double productCost, double productSalePrice, Integer stockIn, Integer stockOut, String productName, BigDecimal netTax, BigDecimal subtotal) {
        this.productCost = productCost;
        this.productSalePrice = productSalePrice;
        this.stockIn = stockIn;
        this.stockOut = stockOut;
        this.productName = productName;
        this.netTax = netTax;
        this.subtotal = subtotal;
    }
}
