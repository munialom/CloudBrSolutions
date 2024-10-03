package com.ctecx.brsuite.autoprintinvoice;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import java.awt.*;
import java.awt.print.*;

class InvoicePrinter implements Printable {
    private Invoice invoice;

    public InvoicePrinter(Invoice invoice) {
        this.invoice = invoice;
    }


    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Set the font
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Draw invoice header
        g2d.drawString("Invoice No.: " + invoice.getInvoiceNo(), 10, 10);
        g2d.drawString("Date: " + invoice.getDate(), 10, 30);

        // Draw Bill To
        g2d.drawString("BILL TO:", 10, 50);
        g2d.drawString(invoice.getBillTo(), 10, 70);

        // Draw table header with border
        String header = "QTY   Item        Description                 Unit Price     VAT         TAX %       Total";
        int y = 90;
        int tableHeight = y -70;

        for (Item item : invoice.getItems()) {
            tableHeight +=20;
        }

        if(tableHeight < pageFormat.getImageableHeight() - y){
            tableHeight = (int)(pageFormat.getImageableHeight() - y);
        }

        // Draw items with border
        for (Item item : invoice.getItems()) {
            String row = item.getQty() + "   " + item.getName() + "   " + item.getDescription() +
                    "   " + item.getUnitPrice() +
                    "   " + item.getVat() +
                    "   "+item.getTax()+"%"+
                    "   "+item.getTotal();
            g2d.drawRect(10,y-20,(int)pageFormat.getImageableWidth()-20 ,tableHeight);
            g2d.drawLine(60,y-20 ,60 ,y+tableHeight-20);
            // Add more lines for each column as needed

            y+=20;
        }

        // Adjusting rectangle size as needed

        // Draw totals outside of bordered area
        y += tableHeight+5;
        g2d.drawString("Subtotal: "+invoice.getSubtotal(),10,y);
        g2d.drawString("VAT: "+invoice.getVat(),10,y+20);
        g2d.drawString("Total: "+invoice.getTotal(),10,y+40);
        g2d.drawString("Balance Due: "+invoice.getBalanceDue(),10,y+60);

        return PAGE_EXISTS;
    }



    public static void printReceipt(Invoice invoice, String printerName) throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

        PrintService selectedService = null;

        for (PrintService service : services) {
            if (service.getName().equals(printerName)) {
                selectedService = service;
                break;
            }
        }

        if (selectedService == null) {
            System.out.println("Printer not found: " + printerName);
            return;
        }

        job.setPrintService(selectedService);

        PrintRequestAttributeSet attrSet = new HashPrintRequestAttributeSet();
        attrSet.add(new Copies(1));
        attrSet.add(new MediaPrintableArea(0, 0, 210, 297, MediaPrintableArea.MM));


        job.setPrintable(new InvoicePrinter(invoice));

        try {
            job.print(attrSet);
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }
}
