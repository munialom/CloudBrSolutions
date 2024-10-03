package com.ctecx.brsuite.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,CustomProductRepository {
    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE %:productName%")
    List<Product> searchProductsByName(@Param("productName") String productName);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE %:productName%")
    Page<Product> searchProductsByName(@Param("productName") String productName, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE %:searchKey% OR LOWER(p.productCode) LIKE %:searchKey%")
    Page<Product> searchProductsByNameOrCode(@Param("searchKey") String searchKey, Pageable pageable);

    boolean existsByProductCode(String productCode);

    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);

    Product findByProductCode(String productCode);
    Product findProductById(Long productId);
}
