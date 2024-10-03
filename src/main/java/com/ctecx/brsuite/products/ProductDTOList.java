package com.ctecx.brsuite.products;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "id",
        "productCode",
        "productName",
        "productCost",
        "productPrice",
        "brandName",
        "categoryName",
        "uom",
        "productType",
        "alertQuantity",
        "taxMode"
})
public class ProductDTOList {

    private Long id;
    private String brandName;
    private String categoryName;
    private String uom;
    private String productType;
    private String productName;
    private String productCode;
    private double productCost;
    private double productPrice;
    private int alertQuantity;
    private String taxMode;

}
