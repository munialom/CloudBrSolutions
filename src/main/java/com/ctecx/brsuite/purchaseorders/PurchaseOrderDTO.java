package com.ctecx.brsuite.purchaseorders;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseOrderDTO {
    private Long vendorId;
    private List<ProductDetailDTO> productDetails;
    private BigDecimal totalAmount;
    private String comments;
    private String paymentTerms;
    private LocalDate transactionDate;
}