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
         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
  List<Map<String, Object>> revenueSummary = customManagerProductService.GetRevenueSummaryByDate(date);
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
         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
  List<Map<String, Object>> waitersSummary = customManagerProductService.GetWaitersSummaryByDate(date);
  return ResponseEntity.ok(waitersSummary);
 }

 @GetMapping("/enhanced-sales-report")
 public ResponseEntity<List<Map<String, Object>>> getEnhancedSalesReport(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
  List<Map<String, Object>> report = customManagerProductService.GetEnhancedSalesReport(startDate, endDate);
  return ResponseEntity.ok(report);
 }

 @GetMapping("/stock-valuation-report")
 public ResponseEntity<List<Map<String, Object>>> getStockValuationReport(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
  List<Map<String, Object>> report = customManagerProductService.GetStockValuationReport(startDate, endDate);
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

}