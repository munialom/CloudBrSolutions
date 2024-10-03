package com.ctecx.brsuite.products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
class BatchInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_details_id")
    private ProductDetails productDetails;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "manufacturing_date")
    private LocalDate manufacturingDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "quantity")
    private int quantity;

    @Override
    public String toString() {
        return String.format(
                "Batch: %s, Mfg: %s, Exp: %s, Qty: %d",
                batchNumber, manufacturingDate, expiryDate, quantity
        );
    }
}