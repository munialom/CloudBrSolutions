package com.ctecx.brsuite.taxes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxClassRepository extends JpaRepository<TaxClass, Long> {
    // You can add custom query methods here if needed
}
