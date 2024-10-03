package com.ctecx.brsuite.autoprintinvoice;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
@Data

@NoArgsConstructor
class Invoice {
    String invoiceNo;
    Date date;
    String billTo;
    ArrayList<Item> items;
    double subtotal;
    double vat;
    double total;
    double balanceDue;

    public Invoice(String invoiceNo, Date date, String billTo, ArrayList<Item> items, double subtotal, double vat, double total, double balanceDue) {
        this.invoiceNo = invoiceNo;
        this.date = date;
        this.billTo = billTo;
        this.items = items;
        this.subtotal = subtotal;
        this.vat = vat;
        this.total = total;
        this.balanceDue = balanceDue;
    }



}