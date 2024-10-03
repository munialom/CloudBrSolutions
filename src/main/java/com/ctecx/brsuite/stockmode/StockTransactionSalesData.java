package com.ctecx.brsuite.stockmode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockTransactionSalesData {

    private String productCode;
    private String productName;
    private Integer openingStock;
    private Integer sales;
    private Integer purchases;
    private Integer adding;
    private Integer subtracting;
    private Integer closing;
   private double salesValue;
   private double purchaseValue;
   private double openingValue;
   private double closingValue;
   private  double profitValue;
}