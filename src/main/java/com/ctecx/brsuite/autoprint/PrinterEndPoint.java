package com.ctecx.brsuite.autoprint;

import com.ctecx.brsuite.customproductsmanager.CustomManagerProductService;
import com.ctecx.brsuite.stockmode.StockTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/print")
@AllArgsConstructor
public class PrinterEndPoint {

    private final StockTransactionService stockTransactionService;
    private final CustomManagerProductService customManagerProductService;


    @GetMapping("/printReceipt/{serialNumber}")
    public ResponseEntity<String> printReceipt(@PathVariable String serialNumber) {

        customManagerProductService.UpdateOpenStatus(serialNumber);
        customManagerProductService.CloseOrderStatus(serialNumber);

        return ResponseEntity.ok("Receipt printed successfully");
    }


}
