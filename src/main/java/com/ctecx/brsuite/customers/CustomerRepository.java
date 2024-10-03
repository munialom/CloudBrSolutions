package com.ctecx.brsuite.customers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByCustomernameContainingIgnoreCase(String keyword);
}
