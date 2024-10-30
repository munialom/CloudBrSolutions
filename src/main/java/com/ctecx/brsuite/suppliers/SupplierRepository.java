package com.ctecx.brsuite.suppliers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Vendor, Long> {


        List<Vendor> findByCreditorNameContainingIgnoreCase(String keyword);


}
