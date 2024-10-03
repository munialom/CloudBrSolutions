package com.ctecx.brsuite.uom;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UomRepository extends JpaRepository<Uom, Long> {
    // You can add custom query methods here if needed
}
