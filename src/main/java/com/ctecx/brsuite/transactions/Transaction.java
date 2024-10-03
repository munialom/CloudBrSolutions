package com.ctecx.brsuite.transactions;


import com.ctecx.brsuite.chartofaccounts.AccountChart;
import com.ctecx.brsuite.customers.Customer;
import com.ctecx.brsuite.util.AuditableBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction extends AuditableBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal amount;
    private BigDecimal credit;
    private BigDecimal debit;
    private String SerialNumber;
    private String description;
    private String status;
    private String accountName;
    private String paymentMode;
    private String module;
    private String ref;
    private String transaction_type;
    private String submodule;
    @ManyToOne
    @JoinColumn(name = "account_chart_id")
    private AccountChart accountChart;

    @Temporal(TemporalType.DATE)
    @Column(name = "transaction_date")
    private LocalDate transactionDate;
    @ManyToOne
    @JoinColumn(name = "customer_custid")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private PaymentState paymentState;
    @Enumerated(EnumType.STRING)
    private OrderState orderState;


}
