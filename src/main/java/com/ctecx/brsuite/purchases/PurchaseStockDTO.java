package com.ctecx.brsuite.purchases;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseStockDTO {
    List<PurchaseStock> purchaseStocks;
    private Long supplierId;
    private Long branchId;
    private LocalDate purchaseDate;
    private String invoiceNumber;
}
