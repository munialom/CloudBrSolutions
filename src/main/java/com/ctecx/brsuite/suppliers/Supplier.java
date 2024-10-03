package com.ctecx.brsuite.suppliers;

import com.ctecx.brsuite.customers.CustomerType;
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
@Table(name = "suppliers")
public class Supplier  extends AuditableBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String creditorName;
    private String creditorAddress;
    private String creditorContact;
    private String creditTerms;
    private String taxPin;
    private BigDecimal creditlimit;
    private String email;
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;
}
