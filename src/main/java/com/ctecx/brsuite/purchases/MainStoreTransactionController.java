package com.ctecx.brsuite.purchases;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main-store")
public class MainStoreTransactionController {

    private final MainStoreTransactionService service;

    @PostMapping("/opening")
    public ResponseEntity<String> createOpeningStock(@RequestBody StoreOpeningStockDTO storeOpeningStockDTO) {
        try {
            service.createOpeningStock(storeOpeningStockDTO);
            return new ResponseEntity<>("Opening stock created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating opening stock: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/purchase")
    public ResponseEntity<String> createPurchase(@RequestBody PurchaseStockDTO purchaseStockDTO) {
        try {
            service.createPurchase(purchaseStockDTO);
            return new ResponseEntity<>("Purchase Saved successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error Saving Purchase Details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/issuance")
    public ResponseEntity<String> createIssuance(@RequestBody IssuanceStockDTO issuanceStockDTO) {
        try {
            service.createIssuance(issuanceStockDTO);
            return new ResponseEntity<>("Issuance created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating issuance: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}