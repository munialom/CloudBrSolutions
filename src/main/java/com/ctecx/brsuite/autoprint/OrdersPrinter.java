package com.ctecx.brsuite.autoprint;


import com.ctecx.brsuite.stockmode.ProductDetail;
import com.ctecx.brsuite.stockmode.StockTransactionDTO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

;

public class OrdersPrinter implements Printable {

    private StockTransactionDTO stockTransactionDTO;


    public OrdersPrinter(StockTransactionDTO transaction) {
        this.stockTransactionDTO = transaction;
    }

    private static Image generateQRCode(String content, int width, int height) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_39, width, height, hints);
            int matrixWidth = bitMatrix.getWidth();
            int matrixHeight = bitMatrix.getHeight();
            int[] pixels = new int[matrixWidth * matrixHeight];
            for (int y = 0; y < matrixHeight; y++) {
                for (int x = 0; x < matrixWidth; x++) {
                    pixels[y * matrixWidth + x] = bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                }
            }
            BufferedImage image = new BufferedImage(matrixWidth, matrixHeight, BufferedImage.TYPE_INT_ARGB);
            image.setRGB(0, 0, matrixWidth, matrixHeight, pixels, 0, matrixWidth);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }







    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
            throws PrinterException {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        int result = NO_SUCH_PAGE;
        if (pageIndex == 0) {
            Graphics2D g2d = (Graphics2D) graphics;
            double width = pageFormat.getImageableWidth();
            g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());

            try {
                int y = 20;
                int yShift = 10;
                int headerRectHeight = 15;
                int centerX = (int) (width / 2);

                g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));

                String[] headerLines = {

                        "PREMIER RESORT CHWELE",
                        "OFF CHWELE -KIMILILI ROAD",
                        "TEL +254718072087",
                        "PAYBILL : 400200",
                        "ACCOUNT : 855177",
                        "-------------------------------------"
                };

                for (String line : headerLines) {
                    int stringWidth = g2d.getFontMetrics().stringWidth(line);
                    int x = centerX - (stringWidth / 2);
                    g2d.drawString(line, x, y);
                    y += yShift;
                }

                y += headerRectHeight;

                // Align the following lines to the left
                g2d.drawString("CAPTAIN ORDER: " + stockTransactionDTO.getOrderNumber(), 10, y);
                y += yShift;
                g2d.drawString("CUSTOMER NAME: " + stockTransactionDTO.getCustomerName(), 10, y);
                y += yShift;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = stockTransactionDTO.getLocalDateTime().format(formatter);
                g2d.drawString("DATE TIME: " + formattedDateTime, 10, y);
                y += headerRectHeight;

                // Center the column headers
                String columnHeader = " Item Name                  Price   ";
                int headerWidth = g2d.getFontMetrics().stringWidth(columnHeader);
                int headerX = centerX - (headerWidth / 2);
                g2d.drawString(columnHeader, headerX, y);
                y += yShift;

                // Center the separator line
                String separator = "-------------------------------------";
                int separatorWidth = g2d.getFontMetrics().stringWidth(separator);
                int separatorX = centerX - (separatorWidth / 2);
                g2d.drawString(separator, separatorX, y);
                y += headerRectHeight;

                for (Map.Entry<String, List<ProductDetail>> entry : stockTransactionDTO.getProductDetailsMap().entrySet()) {
                    for (ProductDetail detail : entry.getValue()) {
                        String itemName = detail.getProductName();
                        int quantity = detail.getStockOut();
                        double itemPrice = detail.getProductSalePrice();
                        BigDecimal subtotal = detail.getNetTax().add(detail.getSubtotal());

                        g2d.drawString(" " + itemName + "                            ", 10, y);
                        y += yShift;
                        g2d.drawString("      " + quantity + " * " + itemPrice, 10, y);
                        g2d.drawString(subtotal.toString(), 160, y);
                        y += yShift;
                    }
                }

                g2d.drawString("-------------------------------------", 10, y);
                y += yShift;

                // Calculate the width of the total string
                String totalString = String.format("%.2f", stockTransactionDTO.getTotal());
                int totalWidth = g2d.getFontMetrics().stringWidth(totalString);

                // Calculate x-coordinate for right alignment (assuming 230 is the right edge of the price column)
                int xTotal = 190 - totalWidth;

                // Draw the total line with right-aligned value
                g2d.drawString("TOTAL:", 10, y);
                g2d.drawString(totalString, xTotal, y);

                y += yShift;
                g2d.drawString("-------------------------------------", 10, y);
                y += yShift;

                y += headerRectHeight;
                double vat = 16;
                String headerFormat = "%-10s | %-10s | %-10s";
                String dataFormat = "%-10s | %-10s | %-10s";
                String headerfoot = String.format(headerFormat, "CODE", "RATE %", "EE AMT");
                String divider = new String(new char[headerfoot.length()]).replace("\0", "-");
                String data = String.format(dataFormat,
                        decimalFormat.format(vat),
                        decimalFormat.format(vat),
                        decimalFormat.format(stockTransactionDTO.getNetTax())
                );
                g2d.drawString(headerfoot, 10, y);
                y += yShift;
                g2d.drawString(divider, 10, y);
                y += yShift;
                g2d.drawString(data, 10, y);
                y += yShift;
                g2d.drawString(divider, 10, y);
                g2d.drawString("-------------------------------------", 10, y);
                y += yShift;
                y += yShift;
                g2d.drawString("SERVED BY: " + stockTransactionDTO.getCreatedBy().toUpperCase(), 10, y);
                y += yShift;
                g2d.drawString("*************************************", 10, y);
                y += yShift;
                g2d.drawString("       THANK YOU COME AGAIN            ", 10, y);
                y += yShift;

                String barcodeContent = " CODE: " + stockTransactionDTO.getSerialNumber();
                Image barcodeImage = generateQRCode(barcodeContent, 70, 30);
                g2d.drawImage(barcodeImage, 9, y, null);
                y += headerRectHeight;
                y += headerRectHeight;
                y += headerRectHeight;
                g2d.drawString("*************************************", 10, y);
                y += yShift;

                g2d.drawString("       SOFTWARE BY:CTECX SYSTEMS          ", 10, y);
                y += yShift;
                g2d.drawString("   CONTACT: ctecxkenya@gmail.com       ", 10, y);
                y += yShift;

            } catch (Exception e) {
                e.printStackTrace();
            }

            result = PAGE_EXISTS;
        }
        return result;
    }





/*    public static void printReceipt(StockTransactionDTO transaction, String printerName) throws PrinterException {
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
        attrSet.add(new MediaPrintableArea(0, 0, 80, 297, MediaPrintableArea.MM));

        job.setPrintable(new OrdersPrinter(transaction));

        try {
            job.print(attrSet);
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }*/

    public static void printReceipt(StockTransactionDTO transaction, String printerName) throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintService selectedService = findPrintService(printerName)
                .orElseThrow(() -> new PrinterException("No suitable printer found for printing receipt."));

        job.setPrintService(selectedService);
        PrintRequestAttributeSet attrSet = new HashPrintRequestAttributeSet();
        attrSet.add(new Copies(1));
        attrSet.add(new MediaPrintableArea(0, 0, 80, 297, MediaPrintableArea.MM));
        job.setPrintable(new ReceiptPrinter(transaction));

        try {
            job.print(attrSet);
            System.out.println("Receipt printed successfully on " + selectedService.getName());
        } catch (PrinterException e) {
            System.err.println("Error printing receipt: " + e.getMessage());
            throw e; // Re-throw the exception to be handled by the caller
        }
    }

    private static Optional<PrintService> findPrintService(String printerName) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

        // First, try to find the exact printer
        Optional<PrintService> exactMatch = Arrays.stream(services)
                .filter(service -> service.getName().equalsIgnoreCase(printerName))
                .findFirst();

        if (exactMatch.isPresent()) {
            return exactMatch;
        }

        // If not found, log a warning and return the default printer
        System.out.println("Warning: Printer '" + printerName + "' not found. Attempting to use default printer."+PrintServiceLookup.lookupDefaultPrintService());
        return Optional.ofNullable(PrintServiceLookup.lookupDefaultPrintService());

    }




}
