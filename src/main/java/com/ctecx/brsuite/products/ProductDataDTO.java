package com.ctecx.brsuite.products;

import com.ctecx.brsuite.brands.Brand;
import com.ctecx.brsuite.productcategory.Category;
import com.ctecx.brsuite.taxes.TaxClass;
import com.ctecx.brsuite.uom.Uom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDataDTO {

    private Long id;
    private Brand brand;
    private Category category;
    private Set<TaxClass> taxClasses;
    private Uom uom;
    private String productType;
    private String productName;
    private String productCode;
    private double productCost;
    private double productPrice;
    private int alertQuantity;
    private String taxMode;

}
