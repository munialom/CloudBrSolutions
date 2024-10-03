package com.ctecx.brsuite.customers;

import com.ctecx.brsuite.util.AuditableBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer extends AuditableBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long custid;
    private String customername;
    private String customeraddress;
    private String customercontact;
    private int creditterm;
    private String taxPin;
    private BigDecimal creditlimit;
    private String email;
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;
}
