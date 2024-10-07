package com.ctecx.brsuite.customproductsmanager;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class CustomProductManagerRepositoryImpl implements CustomProductManagerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Map<String, Object>> GetOpenOrderNumbers() {
        return jdbcTemplate.queryForList("CALL  GetOpenOrderNumbers()");
    }

    @Override
    public List<Map<String, Object>> GetClosedOrderSerialNumbers() {

        return jdbcTemplate.queryForList("CALL GetClosedOrderSerialNumbers()");
    }

    @Override
    public List<Map<String, Object>> getAllCategories() {
        return jdbcTemplate.queryForList("CALL FetchCategories()");
    }


    @Override
    public List<Map<String, Object>> getAllBrands() {

        return jdbcTemplate.queryForList("CALL FetchBrands()");
    }

    @Override
    public List<Map<String, Object>> getAllProducts() {
        return jdbcTemplate.queryForList("CALL GetProductDetails()");
    }

    @Override
    public List<Map<String, Object>> getOpenOrders(String currentUser, LocalDate localDate) {
        return jdbcTemplate.queryForList("CALL GetOpenOrderDetails(?,?)",currentUser,localDate);
    }

    @Override
    public int updateSerialNumbers(String newSerialNumber, List<Long> idList) {
        String idListString = idList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "CALL update_serial_numbers(?, ?)",
                Integer.class,
                newSerialNumber,
                idListString
        )).orElse(0);
    }

    @Override
    public int updateStockTransactionsStatus(String serialNumber) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "CALL update_stock_transactions_status(?)",
                Integer.class,
                serialNumber
        )).orElse(0);
    }

    @Override
    public int CloseOrderStatus(String newSerialNumber) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "CALL CloseOrderStatus(?)",
                Integer.class,
                newSerialNumber
        )).orElse(0);
    }

    @Override
    public String getCategoryByProductName(String productName) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "CALL GetCategoryByProductName(?)",
                String.class,
                productName
        )).orElse("");
    }

    @Override
    public int OrderPrintedStatus(String newSerialNumber) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "CALL OrderPrintedStatus(?)",
                Integer.class,
                newSerialNumber
        )).orElse(0);
    }


    @Override
    public int  cancelOrderStatus(String serialNumber) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "CALL cancel_orders(?)",
                Integer.class,
                serialNumber
        )).orElse(0);
    }


    @Override
    public int  UpdateOpenStatus(String serialNumber) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "CALL update_open_pending(?)",
                Integer.class,
                serialNumber
        )).orElse(0);
    }
    @Override
    public List<Map<String, Object>> GetSalesTransactionsSummary() {

        return jdbcTemplate.queryForList("CALL GetSalesTransactionsSummary()");
    }

    @Override
    public List<Map<String, Object>> GetOrderTransactionDetails(String serialNumber) {
        return jdbcTemplate.queryForList("CALL GetOrderTransactionDetails(?)",serialNumber);
    }

    @Override
    public List<Map<String, Object>> GetRevenueSummaryByDate(LocalDate localDate) {

        return jdbcTemplate.queryForList("CALL GetRevenueSummaryByDate(?)",localDate);
    }

    @Override
    public List<Map<String, Object>> GetSummaryTransactionReport(LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.queryForList("CALL GetSummaryTransactionReport(?,?)",startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> GetWaitersSummaryByDate(LocalDate localDate) {

        return jdbcTemplate.queryForList("CALL GetWaitersSummaryByDate(?)",localDate);
    }

    @Override
    public List<Map<String, Object>> GetEnhancedSalesReport(LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.queryForList("CALL GetEnhancedSalesReport(?,?)",startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> GetStockValuationReport(LocalDate startDate, LocalDate endDate) {

        return jdbcTemplate.queryForList("CALL GetStockValuationReport(?,?)",startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> GetStockValuationSummary() {
        return jdbcTemplate.queryForList("CALL GetStockValuationSummary()");
    }

    @Override
    public List<Map<String, Object>> GetUserInformation() {
        return jdbcTemplate.queryForList("CALL GetUserInformation()");
    }

    @Override
    public List<Map<String, Object>> GetEnhancedSalesReportWaiter(LocalDate startDate, LocalDate endDate, String waiterName) {

        return jdbcTemplate.queryForList("CALL GetEnhancedSalesReportWaiter(?,?,?)",startDate, endDate,waiterName);
    }
    @Override
    public List<Map<String, Object>> getUserInformationSearch(String searchName) {
        return jdbcTemplate.queryForList("CALL GetUserInformationSearch(?)", searchName);
    }

    @Override
    public List<Map<String, Object>> GetEnhancedSummaryData(LocalDate startDate, LocalDate endDate) {

        return jdbcTemplate.queryForList("CALL GetEnhancedSummaryData (?,?)",startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> GetStockValuationReportMeals(LocalDate startDate, LocalDate endDate) {

        return jdbcTemplate.queryForList("CALL GetStockValuationReportMeals(?,?)",startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> GetStockReportStores(LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.queryForList("CALL GetStockReportStores(?,?)",startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> GetProductStockTransactions(int productID) {

        return jdbcTemplate.queryForList("CALL GetProductStockTransactions(?)",productID);
    }

    @Override
    public void deleteProductAndTransactions(Long productId) {
        jdbcTemplate.update("CALL DeleteProductAndTransactions(?)", productId);
    }

    @Override
    public void updateProductNameAndCode(Long productId, String newProductName, String newProductCode) {
        jdbcTemplate.update("CALL UpdateProductNameAndCode(?, ?, ?)", productId, newProductName, newProductCode);
    }

}
