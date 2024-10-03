package com.ctecx.brsuite.products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "packaging_type")
    private String packagingType;

    @Column(name = "purchase_unit")
    private String purchaseUnit;

    @Column(name = "purchase_quantity")
    private int purchaseQuantity;

    @Column(name = "sales_unit")
    private String salesUnit;

    @Column(name = "sales_quantity")
    private int salesQuantity;

    @Column(name = "available_pieces")
    private int availablePieces;

    @Column(name = "available_boxes")
    private int availableBoxes;

    @Column(name = "pieces_per_box")
    private int piecesPerBox;

    @Column(name = "minimum_order_quantity")
    private int minimumOrderQuantity;

    @Column(name = "storage_conditions")
    private String storageConditions;

    @Column(name = "shelf_life")
    private String shelfLife;

    @OneToMany(mappedBy = "productDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BatchInfo> batchInfos = new HashSet<>();

    public String getProductSummary() {
        StringBuilder summary = new StringBuilder(String.format(
                "Product: %s (Code: %s)\n" +
                        "Packaging: %s\n" +
                        "Purchase: %d %s\n" +
                        "Sales: %d %s\n" +
                        "Available: %d pieces, %d boxes\n" +
                        "Minimum Order: %d\n" +
                        "Storage: %s\n" +
                        "Shelf Life: %s\n" +
                        "Batch Information:\n",
                product.getProductName(), product.getProductCode(),
                packagingType,
                purchaseQuantity, purchaseUnit,
                salesQuantity, salesUnit,
                availablePieces, availableBoxes,
                minimumOrderQuantity,
                storageConditions,
                shelfLife
        ));

        for (BatchInfo batchInfo : batchInfos) {
            summary.append(batchInfo.toString()).append("\n");
        }

        return summary.toString();
    }

    public void addBatchInfo(BatchInfo batchInfo) {
        batchInfos.add(batchInfo);
        batchInfo.setProductDetails(this);
    }

    public void removeBatchInfo(BatchInfo batchInfo) {
        batchInfos.remove(batchInfo);
        batchInfo.setProductDetails(null);
    }
}
