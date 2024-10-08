package com.ctecx.brsuite.customproductsmanager;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CustomProductManagerRepository {

    List<Map<String, Object>> GetOpenOrderNumbers();
    List<Map<String, Object>>GetClosedOrderSerialNumbers();

    List<Map<String, Object>> getAllCategories();


    List<Map<String, Object>> getAllBrands();

    List<Map<String, Object>> getAllProducts();
    //getOpenOrders
    List<Map<String, Object>> getOpenOrders(String currentUser, LocalDate localDate);

    // New method for updating serial numbers
    int updateSerialNumbers(String newSerialNumber, List<Long> idList);

    int updateStockTransactionsStatus(String newSerialNumber);

    int CloseOrderStatus(String newSerialNumber);

    String getCategoryByProductName(String productName);

    int OrderPrintedStatus(String newSerialNumber);


    int cancelOrderStatus(String newSerialNumber);

    int UpdateOpenStatus(String newSerialNumber);

    List<Map<String, Object>> GetSalesTransactionsSummary();

    List<Map<String, Object>> GetOrderTransactionDetails(String serialNumber);

    List<Map<String, Object>> GetRevenueSummaryByDate(LocalDate localDate);

    List<Map<String, Object>> GetSummaryTransactionReport(LocalDate startDate, LocalDate endDate);

    List<Map<String, Object>> GetWaitersSummaryByDate(LocalDate localDate);

    List<Map<String, Object>> GetEnhancedSalesReport(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>>GetStockValuationReport(LocalDate startDate, LocalDate endDate);

    List<Map<String, Object>> GetStockValuationSummary();

    List<Map<String, Object>> GetUserInformation();

    List<Map<String, Object>> GetEnhancedSalesReportWaiter(LocalDate startDate, LocalDate endDate,String waiterName);

    List<Map<String, Object>> getUserInformationSearch(String searchName);

    List<Map<String, Object>> GetEnhancedSummaryData(LocalDate startDate, LocalDate endDate);

    List<Map<String, Object>>GetStockValuationReportMeals(LocalDate startDate, LocalDate endDate);

    List<Map<String, Object>>GetStockReportStores(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>>GetProductStockTransactions(int productID);

    void deleteProductAndTransactions(Long productId);
    void updateProductNameAndCode(Long productId, String newProductName, String newProductCode);

    List<Map<String, Object>> GetSupplierCumulativeValue();
    List<Map<String, Object>>GetPurchaseTransactionsReport(LocalDate startDate, LocalDate endDate);

    List<Map<String, Object>> GetAllProducts();

    List<String> findSerialNumbersWithPrefix(String prefix);

    List<String> findOrderNumbersWithPrefix(String prefix);
    boolean existsBySerialNumber(String sn);
    String generateUniqueSerialNumber(String type);

    String generateNewOrderNumber();
}
