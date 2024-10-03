package com.ctecx.brsuite.autoprint;

import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrinterService {

    public List<String> getAllPrinters() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        return Arrays.stream(services)
                .map(PrintService::getName)
                .collect(Collectors.toList());
    }
}