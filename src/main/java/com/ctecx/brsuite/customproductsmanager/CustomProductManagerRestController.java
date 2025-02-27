package com.ctecx.brsuite.customproductsmanager;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/products-manager")
@AllArgsConstructor
public class CustomProductManagerRestController {

 private final CustomManagerProductService customManagerProductService;

 @GetMapping("/cashier-summaries-data")
 public ResponseEntity<List<Map<String, Object>>> GetEnhancedCashierReport(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
  List<Map<String, Object>> report = customManagerProductService.GetEnhancedCashierReport(startDate, endDate);
  return ResponseEntity.ok(report);
 }

 @GetMapping("/categories")
 public ResponseEntity<List<Map<String, Object>>> getAllCategories() {
  List<Map<String, Object>> categories = customManagerProductService.getAllCategories();
  return ResponseEntity.ok(categories);
 }

 @GetMapping("/brands")
 public ResponseEntity<List<Map<String, Object>>> getAllBrands() {
  List<Map<String, Object>> brands = customManagerProductService.getAllBrands();
  return ResponseEntity.ok(brands);
 }

 @GetMapping("/products")
 public ResponseEntity<List<Map<String, Object>>> getAllProducts() {
  List<Map<String, Object>> brands = customManagerProductService.getAllProducts();
  return ResponseEntity.ok(brands);
 }

 @GetMapping("/open-orders")
 public ResponseEntity<List<Map<String, Object>>> getOpenOrders() {
  List<Map<String, Object>> openOrders = customManagerProductService.getOpenOrders();
  return ResponseEntity.ok(openOrders);
 }

 @GetMapping("/sales-transactions-summary")
 public ResponseEntity<List<Map<String, Object>>> getSalesTransactionsSummary() {
  List<Map<String, Object>> summary = customManagerProductService.getSalesTransactionsSummary();
  return ResponseEntity.ok(summary);
 }

 @GetMapping("/order-transaction-details/{serialNumber}")
 public ResponseEntity<List<Map<String, Object>>> getOrderTransactionDetails(@PathVariable String serialNumber) {
  List<Map<String, Object>> details = customManagerProductService.getOrderTransactionDetails(serialNumber);
  return ResponseEntity.ok(details);
 }

 @PostMapping("/cancel-order/{serialNumber}")
 public ResponseEntity<Integer> cancelOrderStatus(@PathVariable String serialNumber) {
  int result = customManagerProductService.cancelOrderStatus(serialNumber);
  return ResponseEntity.ok(result);
 }

 @GetMapping("/orders")
 public ResponseEntity<List<Map<String, Object>>> getOrders() {
  List<Map<String, Object>> details = customManagerProductService.GetOpenOrderNumbers();
  return ResponseEntity.ok(details);
 }

 @GetMapping("/revenue-summary/{date}")
 public ResponseEntity<List<Map<String, Object>>> getRevenueSummaryByDate(
         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
         @RequestParam(required = false) Integer branchId) { // Add branchId as an optional request parameter
  List<Map<String, Object>> revenueSummary = customManagerProductService.GetRevenueSummaryByDate(date, branchId);
  return ResponseEntity.ok(revenueSummary);
 }

 @GetMapping("/summary-transaction-report")
 public ResponseEntity<List<Map<String, Object>>> getSummaryTransactionReport(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
  List<Map<String, Object>> report = customManagerProductService.GetSummaryTransactionReport(startDate, endDate);
  return ResponseEntity.ok(report);
 }

 @GetMapping("/waiters-summary/{date}")
 public ResponseEntity<List<Map<String, Object>>> getWaitersSummaryByDate(
         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
         @RequestParam(required = false) Integer branchId) { // Add branchId as an optional request parameter
  List<Map<String, Object>> waitersSummary = customManagerProductService.GetWaitersSummaryByDate(date, branchId);
  return ResponseEntity.ok(waitersSummary);
 }

 @GetMapping("/enhanced-sales-report")
 public ResponseEntity<List<Map<String, Object>>> getEnhancedSalesReport(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
         @RequestParam int branchId) { // Add branchId as a request parameter
  List<Map<String, Object>> report = customManagerProductService.GetEnhancedSalesReport(startDate, endDate, branchId);
  return ResponseEntity.ok(report);
 }
 @GetMapping("/stock-valuation-report")
 public ResponseEntity<List<Map<String, Object>>> getStockValuationReport(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
         @RequestParam int branchId) {  // Add branchId as a request parameter
  List<Map<String, Object>> report = customManagerProductService.GetStockValuationReport(startDate, endDate, branchId);
  return ResponseEntity.ok(report);
 }

 @GetMapping("/search-sales-transactions")
 public ResponseEntity<List<Map<String, Object>>> searchSalesTransactions(
         @RequestParam(required = true) String serialNumber) {
  List<Map<String, Object>> results = customManagerProductService.searchSalesTransactions(serialNumber);
  return ResponseEntity.ok(results);
 }

 @GetMapping("/running-orders-waiters")
 public ResponseEntity<List<Map<String, Object>>> GetRunningOrdersWaiters(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
         @RequestParam(required = false) String waiterName
 ) {

  List<Map<String, Object>> report = customManagerProductService.GetRunningOrdersWaiters(startDate, endDate, waiterName);

  return ResponseEntity.ok(report);

 }

 @GetMapping("/gas-valuation-report")
 public ResponseEntity<List<Map<String, Object>>> gasValuationReport(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
  List<Map<String, Object>> report = customManagerProductService.GasValuation(startDate, endDate);
  return ResponseEntity.ok(report);
 }


 @GetMapping("/stock-valuation-summary")
 public ResponseEntity<List<Map<String, Object>>> getStockValuationSummary() {
  List<Map<String, Object>> summary = customManagerProductService.GetStockValuationSummaryOverTime();
  return ResponseEntity.ok(summary);
 }


 @GetMapping("/enhanced-sales-report-waiter")
 public ResponseEntity<List<Map<String, Object>>> getEnhancedSalesReportWaiter(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
         @RequestParam String waiterName) {
  List<Map<String, Object>> report = customManagerProductService.GetEnhancedSalesReportWaiter(startDate, endDate, waiterName);
  return ResponseEntity.ok(report);
 }


 @GetMapping("/system-users")
 public ResponseEntity<List<Map<String, Object>>> GetUserInformation() {
  List<Map<String, Object>> summary = customManagerProductService.GetUserInformation();
  return ResponseEntity.ok(summary);
 }
 @GetMapping("/search/{searchName}")
 public ResponseEntity<List<Map<String, Object>>> getUserInformationSearch(@PathVariable String searchName) {
  List<Map<String, Object>> userInfo = customManagerProductService.getUserInformationSearch(searchName);
  return ResponseEntity.ok(userInfo);
 }

 @GetMapping("/enhanced-summary-data")
 public ResponseEntity<List<Map<String, Object>>> getEnhancedSummaryData(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
  List<Map<String, Object>> report = customManagerProductService.GetEnhancedSummaryData(startDate, endDate);
  return ResponseEntity.ok(report);
 }


 @GetMapping("/stock-valuation-report-meals")
 public ResponseEntity<List<Map<String, Object>>> getStockValuationReportMeals(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
  List<Map<String, Object>> report = customManagerProductService.GetStockValuationReportMeals(startDate, endDate);
  return ResponseEntity.ok(report);
 }

 @GetMapping("/stock-report-stores")
 public ResponseEntity<List<Map<String, Object>>> getStockReportStores(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
  List<Map<String, Object>> report = customManagerProductService.GetStockReportStores(startDate, endDate);
  return ResponseEntity.ok(report);
 }

 @GetMapping("/product-stock-transactions/{productID}")
 public ResponseEntity<List<Map<String, Object>>> getProductStockTransactions(@PathVariable int productID) {
  List<Map<String, Object>> transactions = customManagerProductService.GetProductStockTransactions(productID);
  return ResponseEntity.ok(transactions);
 }

 @DeleteMapping("/{id}")
 public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
 customManagerProductService.deleteProductAndTransactions(id);
  return ResponseEntity.noContent().build();
 }


 @DeleteMapping("/byId/{id}")
 public ResponseEntity<Void> DeleteStockTransactionById(@PathVariable Long id) {
  customManagerProductService.DeleteStockTransactionById(id);
  return ResponseEntity.noContent().build();
 }

 @PutMapping("/{id}")
 public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest request) {
  customManagerProductService.updateProductNameAndCode(id, request.getNewName(), request.getNewCode());
  return ResponseEntity.ok().build();
 }

 @GetMapping("/supplier-cumulative-value")
 public ResponseEntity<List<Map<String, Object>>> getSupplierCumulativeValue() {
  List<Map<String, Object>> supplierCumulativeValue = customManagerProductService.GetSupplierCumulativeValue();
  return ResponseEntity.ok(supplierCumulativeValue);
 }


 @GetMapping("/purchase-transactions-report")
 public ResponseEntity<List<Map<String, Object>>> getPurchaseTransactionsReport(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
  List<Map<String, Object>> report = customManagerProductService.GetPurchaseTransactionsReport(startDate, endDate);
  return ResponseEntity.ok(report);
 }


 @GetMapping("/revenue-movement/{date}/{branchId}")
 public ResponseEntity<List<Map<String, Object>>> getRevenueMovement(
         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
         @PathVariable int branchId) {
  List<Map<String, Object>> revenueMovement = customManagerProductService.GetRevenueMovement(date, branchId);
  return ResponseEntity.ok(revenueMovement);
 }

 @GetMapping("/monthly-sales-report")
 public ResponseEntity<List<Map<String, Object>>> getMonthlySalesReport(
         @RequestParam int year,
         @RequestParam int month,
         @RequestParam int branchId) { // Add branchId as a request parameter
  List<Map<String, Object>> report = customManagerProductService.GetMonthlySalesReport(year, month, branchId);
  return ResponseEntity.ok(report);
 }




 @GetMapping("/monthly-sales-report-by-revenue")
 public ResponseEntity<List<Map<String, Object>>> getMonthlySalesReportByRevenue(
         @RequestParam int year,
         @RequestParam int month,
         @RequestParam String revenueName) {
  List<Map<String, Object>> report = customManagerProductService.GetMonthlySalesReportByRevenue(year, month, revenueName);
  return ResponseEntity.ok(report);
 }


 @GetMapping("/yearly-sales-by-revenue")
 public ResponseEntity<List<Map<String, Object>>> getYearlySalesByRevenueCode(
         @RequestParam int year,
         @RequestParam String revenueCode) {
  List<Map<String, Object>> report = customManagerProductService.GetYearlySalesByRevenueCode(year, revenueCode);
  return ResponseEntity.ok(report);
 }


 @GetMapping("/monthly-stock-valuation-report")
 public ResponseEntity<List<Map<String, Object>>> getMonthlyStockValuationReport(
         @RequestParam int year,
         @RequestParam int month,
         @RequestParam int branchId) {
  List<Map<String, Object>> report = customManagerProductService.GetMonthlyStockValuationReport(year, month, branchId);
  return ResponseEntity.ok(report);
 }

 @GetMapping("/monthly-stock-valuation-report-by-category")
 public ResponseEntity<List<Map<String, Object>>> getMonthlyStockValuationReportByCategory(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
         @RequestParam int branchId,
         @RequestParam int categoryId) {
  List<Map<String, Object>> report = customManagerProductService.GetStockValuationReportByCategory(startDate, endDate, branchId, categoryId);
  return ResponseEntity.ok(report);
 }

}