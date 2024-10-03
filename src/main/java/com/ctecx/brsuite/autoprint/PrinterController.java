package com.ctecx.brsuite.autoprint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PrinterController {

    private final PrinterService printerService;

    public PrinterController(PrinterService printerService) {
        this.printerService = printerService;
    }

    @GetMapping("/printers")
    public List<String> getAllPrinters() {
        return printerService.getAllPrinters();
    }
}