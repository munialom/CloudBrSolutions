package com.ctecx.brsuite.products;

import com.ctecx.brsuite.brands.Brand;
import com.ctecx.brsuite.productcategory.Category;
import com.ctecx.brsuite.revenue.Revenue;
import com.ctecx.brsuite.taxes.TaxClass;
import com.ctecx.brsuite.uom.Uom;
import com.ctecx.brsuite.util.AuditableBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AuditableBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "revenue_id")
    private Revenue revenue;


    @ManyToMany(fetch =FetchType.EAGER)
    @JoinTable(name="tax_codes",
            joinColumns =@JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name ="tax_id"))
    private Set<TaxClass> taxClasses= new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "uom_id")
    private Uom uom;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "product_name", columnDefinition = "TEXT")
    private String productName;

    @Column(name = "product_code")
    private String productCode;


    @Column(name = "product_cost")
    private double productCost;

    @Column(name = "product_price")
    private double productPrice;

    @Column(name = "alert_qty")
    private int alertQuantity;

    @Column(name = "tax_mode")
    private String taxMode;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProductDetails productDetails;

    @Column(nullable = true)
    private Integer branchId;

    public String getFullProductInfo() {
        return productDetails != null ? productDetails.getProductSummary() : "Product details not available";
    }


    public boolean hasTax(String taxCode){

        Iterator<TaxClass> taxClassIterator=taxClasses.iterator();
        while(taxClassIterator.hasNext()){
            TaxClass taxClass=taxClassIterator.next();
            if(taxClass.getName().equals(taxCode)){
                return true;
            }

        }
        return false;
    }
}