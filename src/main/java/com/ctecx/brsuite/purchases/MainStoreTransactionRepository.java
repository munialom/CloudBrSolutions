package com.ctecx.brsuite.purchases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MainStoreTransactionRepository extends JpaRepository<MainStoreTransaction, Long> {

    @Query("SELECT t.serialNumber FROM MainStoreTransaction t WHERE t.serialNumber LIKE CONCAT(:prefix, '%') ORDER BY t.serialNumber DESC")
    List<String> findSerialNumbersWithPrefix(@Param("prefix") String prefix);
}