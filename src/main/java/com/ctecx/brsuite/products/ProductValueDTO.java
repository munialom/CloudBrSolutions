package com.ctecx.brsuite.products;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductValueDTO {
    private String productName;
    private String productCode;
    private double productCost;
    private double productPrice;
    private int currentStock;
    private double purchaseValue;
    private double salesValue;
    private double profitMargin;
    private double profit;
}
