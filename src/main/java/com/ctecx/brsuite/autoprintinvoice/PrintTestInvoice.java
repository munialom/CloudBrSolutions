package com.ctecx.brsuite.autoprintinvoice;

import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Date;

public class PrintTestInvoice {


    public static void main(String[] args) {
        // Create some sample items
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Internet", "4 Months internet subscription", 24000.00, 3310.34, 16, 24000.00,1));

        // Create an invoice with the sample items
        Invoice invoice = new Invoice("10000", new Date(), "CHEBUKAKA GIRLS", items, 20689.66, 3310.34, 24000.00, 24000.00);

        // Specify the printer name
        String printerName = "Microsoft Print to PDF";

        // Print the invoice
        try {
           InvoicePrinter.printReceipt(invoice, printerName);
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }
}
