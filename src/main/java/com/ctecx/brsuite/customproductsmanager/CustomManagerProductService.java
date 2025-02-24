package com.ctecx.brsuite.customproductsmanager;


import com.ctecx.brsuite.util.SalesDateTimeManager;
import com.ctecx.brsuite.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomManagerProductService {
    private final CustomProductManagerRepository customProductManagerRepository;

    private final SalesDateTimeManager salesDateTimeManager;


    public List<Map<String, Object>> getAllCategories() {

       return customProductManagerRepository.getAllCategories();
    }

    public List<Map<String, Object>> GetOpenOrderNumbers() {
        return customProductManagerRepository.GetOpenOrderNumbers();
    }

    public Optional<Map<String, Object>> getSingleProductById(Long productId) {
        return customProductManagerRepository.getSingleProductById(productId);
    }
    public List<Map<String, Object>> GetClosedOrderSerialNumbers() {

        return customProductManagerRepository.GetClosedOrderSerialNumbers();
    }

    public List<Map<String, Object>> getAllBrands() {

        return customProductManagerRepository.getAllBrands();
    }

    public List<Map<String, Object>> getAllProducts() {

        return customProductManagerRepository.getAllProducts();
    }

    public List<Map<String, Object>> getOpenOrders() {
        String currentUser= SecurityUtils.getCurrentUserFullName();
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC+3"));
        LocalDate salesDate = salesDateTimeManager.getSalesDate(now);

        return customProductManagerRepository.getOpenOrders(currentUser,salesDate);
    }

    public int updateSerialNumbers(String newSerialNumber, List<Long> idList) {
        return customProductManagerRepository.updateSerialNumbers(newSerialNumber, idList);
    }

    public int updateStatus(String serialNumber){
        return customProductManagerRepository.updateStockTransactionsStatus(serialNumber);
    }

    public int cancelOrderStatus(String serialNumber){
        return customProductManagerRepository.cancelOrderStatus(serialNumber);
    }

    public int CloseOrderStatus(String serialNumber){
        return customProductManagerRepository.CloseOrderStatus(serialNumber);
    }


    public int UpdateOpenStatus(String serialNumber){
        return customProductManagerRepository.UpdateOpenStatus(serialNumber);
    }
    public List<Map<String, Object>> getSalesTransactionsSummary() {
        return customProductManagerRepository.GetSalesTransactionsSummary();
    }

    public List<Map<String, Object>> getOrderTransactionDetails(String serialNumber) {
        return customProductManagerRepository.GetOrderTransactionDetails(serialNumber);
    }

    public List<Map<String, Object>> GetRevenueSummaryByDate(LocalDate localDate,int branchId) {

        return customProductManagerRepository.GetRevenueSummaryByDate(localDate,branchId);
    }

    public List<Map<String, Object>> GetSummaryTransactionReport(LocalDate startDate, LocalDate endDate) {
        return customProductManagerRepository.GetSummaryTransactionReport(startDate, endDate);
    }

    public List<Map<String, Object>> GetWaitersSummaryByDate(LocalDate localDate,int branchId) {

        return customProductManagerRepository.GetWaitersSummaryByDate(localDate,branchId);
    }

    public List<Map<String, Object>> GetEnhancedSalesReport(LocalDate startDate, LocalDate endDate,int branchId) {
        return customProductManagerRepository.GetEnhancedSalesReport(startDate,endDate,branchId);
    }



    public List<Map<String, Object>> GetStockValuationSummaryOverTime() {
        return customProductManagerRepository.GetStockValuationSummary();
    }

    public List<Map<String, Object>> GetEnhancedSalesReportWaiter(LocalDate startDate, LocalDate endDate, String waiterName) {

        return customProductManagerRepository.GetEnhancedSalesReportWaiter(startDate,endDate,waiterName);
    }

    public List<Map<String, Object>> GetUserInformation() {
        return customProductManagerRepository.GetUserInformation();
    }

    public List<Map<String, Object>> getUserInformationSearch(String searchName) {
        return customProductManagerRepository.getUserInformationSearch(searchName);
    }

    public List<Map<String, Object>> GetEnhancedSummaryData(LocalDate startDate, LocalDate endDate) {

        return customProductManagerRepository.GetEnhancedSummaryData(startDate, endDate);
    }


    public List<Map<String, Object>> GetStockValuationReportMeals(LocalDate startDate, LocalDate endDate) {

        return customProductManagerRepository.GetStockValuationReportMeals(startDate, endDate);
    }

    public List<Map<String, Object>> GetStockReportStores(LocalDate startDate, LocalDate endDate) {
        return customProductManagerRepository.GetStockReportStores(startDate, endDate);
    }

    public List<Map<String, Object>> GetStockValuationReport(LocalDate startDate, LocalDate endDate,int branchId) {

        return customProductManagerRepository.GetStockValuationReport(startDate,endDate,branchId);
    }


    public List<Map<String, Object>> GasValuation(LocalDate startDate, LocalDate endDate) {
        return customProductManagerRepository.GasValuation(startDate,endDate);
    }


    public List<Map<String, Object>> GetProductStockTransactions(int productID) {

        return customProductManagerRepository.GetProductStockTransactions(productID);
    }



    @Transactional
    public void deleteProductAndTransactions(Long productId) {
        customProductManagerRepository.deleteProductAndTransactions(productId);
    }


    @Transactional
    public void updateProductNameAndCode(Long productId, String newProductName, String newProductCode) {
        customProductManagerRepository.updateProductNameAndCode(productId, newProductName, newProductCode);
    }

    public List<Map<String, Object>> GetSupplierCumulativeValue() {

        return   customProductManagerRepository.GetSupplierCumulativeValue();
    }

    public List<Map<String, Object>> GetPurchaseTransactionsReport(LocalDate startDate, LocalDate endDate) {

        return customProductManagerRepository.GetPurchaseTransactionsReport(startDate, endDate);
    }

    public List<Map<String, Object>> GetAllProducts() {

        return   customProductManagerRepository.GetAllProducts();
    }


    public List<Map<String, Object>> GetRevenueMovement(LocalDate localDate,int branchId) {

        return   customProductManagerRepository.GetRevenueMovement(localDate,branchId);
    }


    public List<Map<String, Object>> GetMonthlySalesReport(int currentYear, int currentMonth, int branchId) {

        return customProductManagerRepository.GetMonthlySalesReport(currentYear, currentMonth,branchId);
    }

    public List<Map<String, Object>> GetMonthlySalesReportByRevenue(int currentYear, int currentMonth, String revenueName) {

        return customProductManagerRepository.GetMonthlySalesReportByRevenue(currentYear, currentMonth,revenueName);
    }


    public List<Map<String, Object>> GetYearlySalesByRevenueCode(int currentYear, String revenueName) {

        return customProductManagerRepository.GetYearlySalesByRevenueCode(currentYear, revenueName);
    }

    public List<Map<String, Object>> GetMonthlyStockValuationReport(int currentYear, int currentMonth,int branchId) {

        return customProductManagerRepository.GetMonthlyStockValuationReport(currentYear, currentMonth,branchId);
    }

    public List<Map<String, Object>> GetStockValuationReportByCategory(LocalDate startDate, LocalDate endDate, int branchId, int categoryId) {

        return customProductManagerRepository.GetStockValuationReportByCategory(startDate, endDate,branchId,categoryId);
    }


    public Optional<Map<String, Object>> getSingleProductByCode(String productCode) {
        return customProductManagerRepository.getSingleProductByCode(productCode);
    }

}
