package com.ctecx.brsuite.purchases;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuanceStock {
    private String productCode;
    private int qty;
    private double unitCost;
}
