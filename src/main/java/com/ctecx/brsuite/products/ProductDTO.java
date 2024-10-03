package com.ctecx.brsuite.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private Long brandId;
    private Long categoryId;
    private Long revenueId;
    private Set<Long> taxClassIds;
    private Long uomId;
    private String productType;
    private String productName;
    private String productCode;
    private double productCost;
    private double productPrice;
    private int alertQuantity;
    private String taxMode;

}