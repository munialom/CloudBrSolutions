package com.ctecx.brsuite.revenue;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RevenueRepository extends JpaRepository<Revenue, Long> {

    List<Revenue> findByRevenueNameContainingIgnoreCase(String keyword);

    Optional<Revenue> findByRevenueName(String revenueName);
}