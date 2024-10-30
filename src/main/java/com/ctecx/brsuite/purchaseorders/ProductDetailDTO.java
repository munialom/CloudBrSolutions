package com.ctecx.brsuite.purchaseorders;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDetailDTO {
    private Long productId;
    private String productCode;
    private Integer quantityRequested;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}