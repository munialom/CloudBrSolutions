package com.ctecx.brsuite.requisitions;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RequisitionNumberGenerator {
    private final EntityManager entityManager;
    private static final String PREFIX = "REQ";

    public String generateRequisitionNumber() {
        int currentYear = Year.now().getValue();
        String yearPrefix = String.valueOf(currentYear).substring(2);

        var query = entityManager.createQuery(
                        "SELECT r.requisitionNumber FROM Requisition r " +
                                "WHERE r.requisitionNumber LIKE :prefix " +
                                "ORDER BY r.requisitionNumber DESC")
                .setParameter("prefix", PREFIX + "-" + yearPrefix + "%")
                .setMaxResults(1);

        Optional<String> lastNumber = query.getResultList().stream().findFirst();

        return lastNumber
                .map(last -> {
                    int sequence = Integer.parseInt(last.substring(7)) + 1;
                    return String.format("%s-%s-%04d", PREFIX, yearPrefix, sequence);
                })
                .orElse(String.format("%s-%s-%04d", PREFIX, yearPrefix, 1));
    }
}