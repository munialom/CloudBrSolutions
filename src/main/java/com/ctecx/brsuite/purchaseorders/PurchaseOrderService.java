package com.ctecx.brsuite.purchaseorders;


import com.ctecx.brsuite.products.Product;
import com.ctecx.brsuite.products.ProductRepository;
import com.ctecx.brsuite.suppliers.SupplierRepository;
import com.ctecx.brsuite.suppliers.Vendor;
import com.ctecx.brsuite.util.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository vendorRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderNumberGenerator orderNumberGenerator;

    @Transactional
    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO dto) {
        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        List<PurchaseOrder> savedOrders = new ArrayList<>();
        String orderNumber = orderNumberGenerator.generateOrderNumber();

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ProductDetailDTO detail : dto.getProductDetails()) {
            Product product = productRepository.findByProductCode(detail.getProductCode());
            if (product == null) {
                throw new ResourceNotFoundException("Product not found with code: " + detail.getProductCode());
            }

            PurchaseOrder order = new PurchaseOrder();
            order.setOrderNumber(orderNumber);
            order.setVendor(vendor);
            order.setProduct(product);
            order.setQuantityRequested(detail.getQuantityRequested());
            order.setUnitPrice(detail.getUnitPrice());
            order.setStatus(PurchaseOrderStatus.PENDING);
            order.setComments(dto.getComments());
            order.setPaymentTerms(dto.getPaymentTerms());
            order.setTransactionDate(dto.getTransactionDate());

            PurchaseOrder savedOrder = purchaseOrderRepository.save(order);
            savedOrders.add(savedOrder);
            totalAmount = totalAmount.add(savedOrder.getTotalAmount());
        }

        return mapToDTO(savedOrders, totalAmount);
    }

    @Transactional
    public PurchaseOrderDTO updatePurchaseOrder(Long id, PurchaseOrderDTO dto) {
        PurchaseOrder existingOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found"));

        List<PurchaseOrder> existingOrders = purchaseOrderRepository
                .findByOrderNumber(existingOrder.getOrderNumber());

        purchaseOrderRepository.deleteAll(existingOrders);
        return createPurchaseOrder(dto);
    }

    public PurchaseOrderDTO getPurchaseOrder(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found"));

        List<PurchaseOrder> relatedOrders = purchaseOrderRepository
                .findByOrderNumber(order.getOrderNumber());

        BigDecimal totalAmount = relatedOrders.stream()
                .map(PurchaseOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return mapToDTO(relatedOrders, totalAmount);
    }

    @Transactional
    public void deletePurchaseOrder(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found"));

        List<PurchaseOrder> relatedOrders = purchaseOrderRepository
                .findByOrderNumber(order.getOrderNumber());

        purchaseOrderRepository.deleteAll(relatedOrders);
    }

    private PurchaseOrderDTO mapToDTO(List<PurchaseOrder> orders, BigDecimal totalAmount) {
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found");
        }

        PurchaseOrder firstOrder = orders.get(0);
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setVendorId(firstOrder.getVendor().getId());
        dto.setComments(firstOrder.getComments());
        dto.setPaymentTerms(firstOrder.getPaymentTerms());
        dto.setTransactionDate(firstOrder.getTransactionDate());
        dto.setTotalAmount(totalAmount);

        List<ProductDetailDTO> productDetails = new ArrayList<>();
        for (PurchaseOrder order : orders) {
            ProductDetailDTO detail = new ProductDetailDTO();
            detail.setProductId(order.getProduct().getId());
            detail.setProductCode(order.getProduct().getProductCode());
            detail.setQuantityRequested(order.getQuantityRequested());
            detail.setUnitPrice(order.getUnitPrice());
            productDetails.add(detail);
        }
        dto.setProductDetails(productDetails);

        return dto;
    }
}