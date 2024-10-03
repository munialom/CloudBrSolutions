package com.ctecx.brsuite.stockmode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    // You can add custom query methods here if needed

    @Query("SELECT t.serialNumber FROM StockTransaction t WHERE t.serialNumber LIKE CONCAT(:prefix, '%') ORDER BY t.serialNumber DESC")
    List<String> findSerialNumbersWithPrefix(@Param("prefix") String prefix);

    @Query("SELECT s.orderNumber FROM StockTransaction s WHERE s.orderNumber LIKE CONCAT(:prefix, '%') ORDER BY s.orderNumber DESC")
    List<String> findOrderNumbersWithPrefix(@Param("prefix") String prefix);

    List<StockTransaction> findBySerialNumber(String serialNumber);

    List<StockTransaction> findByOrderNumber(String orderNumber);

    @Query(value = "CALL GetStockReport(:date);", nativeQuery = true)
    Stream<Object[]> getStockReportStream(@Param("date") LocalDate date);

    List<StockTransaction> findByProductIdIn(List<Long> productIds);

    boolean existsBySerialNumber(String sn);
}
