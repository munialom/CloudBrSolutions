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

    public List<Map<String, Object>> GetRevenueSummaryByDate(LocalDate localDate) {

        return customProductManagerRepository.GetRevenueSummaryByDate(localDate);
    }

    public List<Map<String, Object>> GetSummaryTransactionReport(LocalDate startDate, LocalDate endDate) {
        return customProductManagerRepository.GetSummaryTransactionReport(startDate, endDate);
    }

    public List<Map<String, Object>> GetWaitersSummaryByDate(LocalDate localDate) {

        return customProductManagerRepository.GetWaitersSummaryByDate(localDate);
    }

    public List<Map<String, Object>> GetEnhancedSalesReport(LocalDate startDate, LocalDate endDate) {
        return customProductManagerRepository.GetEnhancedSalesReport(startDate,endDate);
    }

  /*  public List<Map<String, Object>> GetStockValuationReport(LocalDate startDate, LocalDate endDate) {

        return customProductManagerRepository.GetStockValuationReport(startDate,endDate);
    }*/

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

    public List<Map<String, Object>> GetStockValuationReport(LocalDate startDate, LocalDate endDate) {

        return customProductManagerRepository.GetStockValuationReport(startDate,endDate);
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
}
