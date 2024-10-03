package com.ctecx.brsuite.packproducts;


import com.ctecx.brsuite.packages.PackageType;
import com.ctecx.brsuite.products.Product;
import com.ctecx.brsuite.util.AuditableBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "package_products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageProduct extends AuditableBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private PackageType packageType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "package_products",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();

    private int quantity;
}
