package com.ctecx.brsuite.autoprintinvoice;

import lombok.Data;

@Data
class Item {
    String name;
    String description;
    double unitPrice;
    double vat;
    double tax;
    double total;
    int qty;

    public Item(String name, String description, double unitPrice, double vat, double tax, double total,int qty) {
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.vat = vat;
        this.tax = tax;
        this.qty=qty;
        this.total = total;
    }
}