package com.ctecx.brsuite.stockmode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {


    @Query("SELECT t.serialNumber FROM StockTransaction t WHERE t.serialNumber LIKE CONCAT(:prefix, '%') ORDER BY t.serialNumber DESC")
    List<String> findSerialNumbersWithPrefix(@Param("prefix") String prefix);

    @Query("SELECT s.orderNumber FROM StockTransaction s WHERE s.orderNumber LIKE CONCAT(:prefix, '%') ORDER BY s.orderNumber DESC")
    List<String> findOrderNumbersWithPrefix(@Param("prefix") String prefix);




    boolean existsBySerialNumber(String sn);
}
