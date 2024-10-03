package com.ctecx.brsuite.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    private String qty;
    private String item;
    private String description;
    private String unitPrice;
    private String vat;
    private String taxPercent;
    private String total;

}
