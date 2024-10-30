package com.ctecx.brsuite.requisitions;


import com.ctecx.brsuite.products.Product;
import com.ctecx.brsuite.util.AuditableBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requisitions")
public class Requisition extends AuditableBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String requisitionNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantityRequested;

    @Column(nullable = false)
    private Integer quantityIssued;

    @Column(nullable = false)
    private Integer remainingQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequisitionStatus status;


    private String comments;


    @PrePersist
    private void prePersist() {
        this.quantityIssued = 0;
        this.remainingQuantity = this.quantityRequested;
        if (this.status == null) {
            this.status = RequisitionStatus.PENDING;
        }
    }
}