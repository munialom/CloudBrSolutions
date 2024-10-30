package com.ctecx.brsuite.purchaseorders;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PurchaseOrderNumberGenerator {
    private final EntityManager entityManager;
    private static final String PREFIX = "PO";

    public String generateOrderNumber() {
        int currentYear = Year.now().getValue();
        String yearPrefix = String.valueOf(currentYear).substring(2);

        Query query = entityManager.createQuery(
                        "SELECT p.orderNumber FROM PurchaseOrder p " +
                                "WHERE p.orderNumber LIKE :prefix " +
                                "ORDER BY p.orderNumber DESC")
                .setParameter("prefix", PREFIX + "-" + yearPrefix + "%")
                .setMaxResults(1);

        Optional<String> lastNumber = query.getResultList().stream().findFirst();

        if (lastNumber.isPresent()) {
            String lastOrderNumber = lastNumber.get();
            int sequence = Integer.parseInt(lastOrderNumber.substring(6)) + 1;
            return String.format("%s-%s-%04d", PREFIX, yearPrefix, sequence);
        } else {
            return String.format("%s-%s-%04d", PREFIX, yearPrefix, 1);
        }
    }
}