package com.ctecx.brsuite.stockmode;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockRestController {

    private final StockService stockService;
    private final SalesService salesService;
    private final StockTransactionService stockTransactionService;

    @GetMapping("/generate/serial-number")
    public ResponseEntity<String> generateSerialNumber(@RequestParam String type) {
        try {
            String serialNumber = salesService.generateSerialNumber(type);
            return ResponseEntity.ok(serialNumber);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/split-orders")
    public ResponseEntity<String> splitOrders(@RequestBody SplitOrderDTO splitOrderDTO) {
        String serialNumber = salesService.splitOrders(splitOrderDTO);
        return ResponseEntity.ok("Orders split and assigned serial number: " + serialNumber);
    }




    @PostMapping("/opening")
    public ResponseEntity<String> createOpeningStock(@RequestBody OpeningStockDTO openingStockDTO) {
        try {
            stockService.createOpeningStock(openingStockDTO);
            return new ResponseEntity<>("Opening stock created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating opening stock: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/prices")
    public ResponseEntity<String> updateprices(@RequestBody OpeningStockDTO openingStockDTO) {
        try {
            stockService.updatePrices(openingStockDTO);
            return new ResponseEntity<>("Prices of  stock updated successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating stock prices: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/purchase")
    public ResponseEntity<String> createPurchase(@RequestBody PurchaseStockDTO purchaseStockDTO) {
        try {
            stockService.createPurchase(purchaseStockDTO);
            return new ResponseEntity<>("Purchase Saved successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error Saving Purchase Details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }






    @PostMapping("/counter-sales")
    public ResponseEntity<Map<String, String>> createSalesCounter(@RequestBody SalesStockDTO salesStockDTO) {
        String transactionId = salesService.createCounterSales(salesStockDTO);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Sales Transaction Saved successfully");
        response.put("transactionId", transactionId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/sales")
    public ResponseEntity<Map<String, String>> createSales(
            @RequestBody SalesStockDTO salesStockDTO,
            HttpServletRequest request) {

        // Get the client's IP address



        // Use the client's IP address in the service call
        String transactionId = salesService.createSalesWaiters(salesStockDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Sales Transaction Saved successfully");
        response.put("transactionId", transactionId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



}
