package com.ctecx.brsuite.autoprint;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrinterConfig {
    private static String kotPrinter;
    private static String waitersPrinter;

    @Value("${pos.printer.kot}")
    public void setKotPrinter(String printer) {
        PrinterConfig.kotPrinter = printer;
    }

    @Value("${pos.printer.waiters}")
    public void setWaitersPrinter(String printer) {
        PrinterConfig.waitersPrinter = printer;
    }

    public static String getKotPrinter() {
        return kotPrinter;
    }

    public static String getWaitersPrinter() {
        return waitersPrinter;
    }
}