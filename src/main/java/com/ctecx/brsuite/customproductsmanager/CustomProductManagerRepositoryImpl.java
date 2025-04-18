package com.ctecx.brsuite.customproductsmanager;


import com.ctecx.brsuite.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDate;
import java.util.HashMap;
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
    public Optional<Map<String, Object>> getSingleProductById(Long productId) {
        String sql = "CALL GetSingleProductById(?)";
        return jdbcTemplate.query(sql,
                (PreparedStatement ps) -> ps.setLong(1, productId),
                (ResultSet rs) -> {
                    if (rs.next()) {
                        Map<String, Object> result = new HashMap<>();
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            result.put(metaData.getColumnName(i), rs.getObject(i));
                        }
                        return Optional.of(result);
                    }
                    return Optional.empty();
                }
        );
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
        Integer branchId = Math.toIntExact(SecurityUtils.getCurrentUserBranch().getId());
        String sql = "CALL GetProductDetails(?)";
        return jdbcTemplate.queryForList(sql,branchId);

        /*return jdbcTemplate.queryForList("CALL GetProductDetails(?)");*/
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
    public List<Map<String, Object>> GetRevenueSummaryByDate(LocalDate localDate,int branchId) {

        return jdbcTemplate.queryForList("CALL GetRevenueSummaryByDate(?,?)",localDate,branchId);
    }

    @Override
    public List<Map<String, Object>> GetSummaryTransactionReport(LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.queryForList("CALL GetSummaryTransactionReport(?,?)",startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> GetWaitersSummaryByDate(LocalDate localDate,int branchId) {

        return jdbcTemplate.queryForList("CALL GetWaitersSummaryByDate(?,?)",localDate,branchId);
    }

    @Override
    public List<Map<String, Object>> GetEnhancedSalesReport(LocalDate startDate, LocalDate endDate,int  branchId) {
        return jdbcTemplate.queryForList("CALL GetEnhancedSalesReport(?,?,?)",startDate, endDate,branchId);
    }

    @Override
    public List<Map<String, Object>> GetStockValuationReport(LocalDate startDate, LocalDate endDate,int  branchId) {

        return jdbcTemplate.queryForList("CALL GetStockValuationReport(?,?,?)",startDate, endDate,branchId);
    }

    @Override
    public List<Map<String, Object>> GasValuation(LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.queryForList("CALL GasValuation(?,?)",startDate, endDate);
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
    public List<Map<String, Object>> GetRunningOrdersByWaiters(LocalDate startDate, LocalDate endDate, String waiterName) {
        Integer branchId = Math.toIntExact(SecurityUtils.getCurrentUserBranch().getId());
        String sql = "CALL GetRunningOrdersByWaiters(?,?,?,?)";
        return jdbcTemplate.queryForList(sql,startDate, endDate,waiterName,branchId);
    }
    @Override
    public List<Map<String, Object>> GetEnhancedCashierReport(LocalDate startDate, LocalDate endDate) {

        Integer branchId = Math.toIntExact(SecurityUtils.getCurrentUserBranch().getId());
        String sql = "CALL GetEnhancedCashierReport(?,?,?)";
        return jdbcTemplate.queryForList(sql,startDate, endDate,branchId);
    }

    @Override
    public List<Map<String, Object>> SearchSalesTransactions(String serialNumber) {
        Integer branchId = Math.toIntExact(SecurityUtils.getCurrentUserBranch().getId());
        String sql = "CALL SearchSalesTransactions(?,?)";

        return jdbcTemplate.queryForList(sql, serialNumber,branchId);
    }


    @Override
    public void DeleteStockTransactionById(Long transactionId) {
        jdbcTemplate.update("CALL DeleteStockTransactionById(?)", transactionId);
    }

    @Override
    public void updateProductNameAndCode(Long productId, String newProductName, String newProductCode) {
        jdbcTemplate.update("CALL UpdateProductNameAndCode(?, ?, ?)", productId, newProductName, newProductCode);
    }

    @Override
    public List<Map<String, Object>> GetSupplierCumulativeValue() {

        return jdbcTemplate.queryForList("CALL GetSupplierCumulativeValue()");
    }

    @Override
    public List<Map<String, Object>> GetPurchaseTransactionsReport(LocalDate startDate, LocalDate endDate) {

        return jdbcTemplate.queryForList("CALL GetPurchaseTransactionsReport(?,?)",startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> GetAllProducts() {

        return jdbcTemplate.queryForList("CALL GetAllProducts()");
    }

    @Override
    public List<String> findSerialNumbersWithPrefix(String prefix) {
        return jdbcTemplate.queryForList("CALL FindSerialNumbersWithPrefix(?)", String.class, prefix);
    }

    @Override
    public List<String> findOrderNumbersWithPrefix(String prefix) {
        return jdbcTemplate.queryForList("CALL FindOrderNumbersWithPrefix(?)", String.class, prefix);
    }

    @Override
    public boolean existsBySerialNumber(String sn) {
        return jdbcTemplate.queryForObject("CALL ExistsBySerialNumber(?)", Boolean.class, sn);
    }

    @Override
    public String generateUniqueSerialNumber(String type) {
        return jdbcTemplate.queryForObject(
                "CALL GenerateUniqueSerialNumber(?)",
                new Object[]{type},
                (rs, rowNum) -> rs.getString("generated_serial_number")
        );
    }

    @Override
    public String generateNewOrderNumber() {
        return jdbcTemplate.queryForObject(
                "CALL GenerateNewOrderNumber()",
                (rs, rowNum) -> rs.getString("generated_order_number")
        );
    }

    @Override
    public List<Map<String, Object>> GetRevenueMovement(LocalDate localDate,int branchId) {

        return jdbcTemplate.queryForList("CALL  GetRevenueMovement(?,?)",localDate,branchId);
    }


    @Override
    public List<Map<String, Object>> GetMonthlySalesReport(int currentYear, int currentMonth, int branchId) {

        return jdbcTemplate.queryForList("CALL GetMonthlySalesReport(?,?,?)",currentYear, currentMonth,branchId);
    }

    @Override
    public List<Map<String, Object>> GetMonthlySalesReportByRevenue(int currentYear, int currentMonth, String revenueName) {

        return jdbcTemplate.queryForList("CALL GetMonthlySalesReportByRevenue(?,?,?)",currentYear, currentMonth, revenueName);
    }

    @Override
    public List<Map<String, Object>> GetYearlySalesByRevenueCode(int currentYear, String revenueName) {

        return jdbcTemplate.queryForList("CALL GetYearlySalesByRevenueCode(?,?)",currentYear,  revenueName);
    }

    @Override
    public List<Map<String, Object>> GetMonthlyStockValuationReport(int currentYear, int currentMonth,int branchId) {

        return jdbcTemplate.queryForList("CALL GetStockValuationReportMonthlyData(?,?,?)",currentYear, currentMonth,branchId);
    }

    @Override
    public List<Map<String, Object>> GetStockValuationReportByCategory(LocalDate startDate, LocalDate endDate, int branchId, int categoryId) {
        return jdbcTemplate.queryForList("CALL GetStockValuationReportByCategory(?,?,?,?)",
                startDate, endDate, branchId, categoryId);
    }

    @Override
    public Optional<Map<String, Object>> getSingleProductByCode(String productCode) {
        String sql = "CALL GetSingleProductByCode(?)";
        return jdbcTemplate.query(sql,
                (PreparedStatement ps) -> ps.setString(1, productCode),
                (ResultSet rs) -> {
                    if (rs.next()) {
                        Map<String, Object> result = new HashMap<>();
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            result.put(metaData.getColumnName(i), rs.getObject(i));
                        }
                        return Optional.of(result);
                    }
                    return Optional.empty();
                }
        );
    }
}
