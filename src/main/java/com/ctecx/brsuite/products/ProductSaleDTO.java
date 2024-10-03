package com.ctecx.brsuite.products;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSaleDTO {
    private String productName;
    private String productCode;
    private double productCost;
    private double productPrice;
    private int currentStock;
    private double tax;


}
