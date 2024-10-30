package com.ctecx.brsuite.purchaseorders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByVendorId(Long vendorId);
    List<PurchaseOrder> findByStatus(PurchaseOrderStatus status);
    boolean existsByOrderNumber(String orderNumber);

    List<PurchaseOrder> findByOrderNumber(String orderNumber);
}