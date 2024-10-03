package com.ctecx.brsuite.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByStoreNameContainingIgnoreCase(String keyword);
    // Updated method to find the first counter store
    Optional<Store> findFirstByCounterStoreTrue();
}
