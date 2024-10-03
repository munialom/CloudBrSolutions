package com.ctecx.brsuite.purchases;


import com.ctecx.brsuite.customers.Customer;
import com.ctecx.brsuite.products.Product;
import com.ctecx.brsuite.suppliers.Supplier;
import com.ctecx.brsuite.util.AuditableBase;
import com.ctecx.brsuite.warehouse.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "main_store_transactions")
public class MainStoreTransaction extends AuditableBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private String productCode;
    private double productCost;
    private BigDecimal discount;
    private BigDecimal tax;
    private double productSalePrice;
    @Temporal(TemporalType.DATE) // Specify the temporal type as DATE
    @Column(name = "transaction_date")
    private LocalDate transactionDate;
    private String module;
    private String serialNumber;
    private String description;
    private String status;
    private String subModule;
    private Integer stockIn;
    private Integer stockOut;
    private String productName;

}