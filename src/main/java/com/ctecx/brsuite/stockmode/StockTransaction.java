package com.ctecx.brsuite.stockmode;

import com.ctecx.brsuite.customers.Customer;
import com.ctecx.brsuite.products.Product;
import com.ctecx.brsuite.revenue.Revenue;
import com.ctecx.brsuite.suppliers.Vendor;
import com.ctecx.brsuite.transactions.OrderState;
import com.ctecx.brsuite.transactions.PaymentState;
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
@Table(name = "stock_transactions")
public class StockTransaction extends AuditableBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Vendor vendor;

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
    private BigDecimal netTax;
    private BigDecimal subtotal;
    private BigDecimal changeOut;
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

    @Column(columnDefinition = "TEXT")
    private String productName;
    @ManyToOne
    @JoinColumn(name = "revenue_id")
    private Revenue revenue;
    private String revenue_code;
    @Enumerated(EnumType.STRING)
    private PaymentState paymentState;
    @Enumerated(EnumType.STRING)
    private OrderState orderState;
    private String orderNumber;
    private boolean orderPrinted;
    private boolean orderClosed;
    private boolean reprint;
    private boolean mainPrinted;
}
