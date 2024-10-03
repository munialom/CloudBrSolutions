package com.ctecx.brsuite.reports;

import com.ctecx.brsuite.customproductsmanager.CustomProductManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final CustomProductManagerRepository repository;
    private final DynamicReportGenerator reportGenerator;

    public Path generateRevenueSummaryReport(LocalDate localDate) {
        List<Map<String, Object>> reportData = repository.GetRevenueSummaryByDate(localDate);
        Map<String, String> additionalInfo = new HashMap<>();
        additionalInfo.put("Date", localDate.toString());
        return generateReport(reportData, "Revenue Summary Report", "revenue_summary_" + localDate + ".pdf", additionalInfo);
    }

    public Path generateSummaryTransactionReport(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> reportData = repository.GetSummaryTransactionReport(startDate, endDate);
        Map<String, String> additionalInfo = new HashMap<>();
        additionalInfo.put("Period", startDate + " to " + endDate);
        return generateReport(reportData, "Summary Transaction Report", "summary_transaction_" + startDate + "_to_" + endDate + ".pdf", additionalInfo);
    }

    public Path generateWaitersSummaryReport(LocalDate localDate) {
        List<Map<String, Object>> reportData = repository.GetWaitersSummaryByDate(localDate);
        Map<String, String> additionalInfo = new HashMap<>();
        additionalInfo.put("Date", localDate.toString());
        return generateReport(reportData, "Waiters Summary Report", "waiters_summary_" + localDate + ".pdf", additionalInfo);
    }

    public Path generateEnhancedSalesReport(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> reportData = repository.GetEnhancedSalesReport(startDate, endDate);
        Map<String, String> additionalInfo = new HashMap<>();
        additionalInfo.put("Period", startDate + " to " + endDate);
        return generateReport(reportData, "Enhanced Sales Report", "enhanced_sales_" + startDate + "_to_" + endDate + ".pdf", additionalInfo);
    }

    public Path generateStockValuationReport(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> reportData = repository.GetStockValuationReport(startDate, endDate);
        Map<String, String> additionalInfo = new HashMap<>();
        additionalInfo.put("Period", startDate + " to " + endDate);
        return generateReport(reportData, "Stock Valuation Report", "stock_valuation_" + startDate + "_to_" + endDate + ".pdf", additionalInfo);
    }

    public Path generateEnhancedSalesReportWaiter(LocalDate startDate, LocalDate endDate, String waiterName) {
        List<Map<String, Object>> reportData = repository.GetEnhancedSalesReportWaiter(startDate, endDate, waiterName);
        Map<String, String> additionalInfo = new HashMap<>();
        additionalInfo.put("Period", startDate + " to " + endDate);
        additionalInfo.put("Waiter", waiterName);
        return generateReport(reportData, "Enhanced Sales Report (Waiter)", "enhanced_sales_waiter_" + startDate + "_to_" + endDate + ".pdf", additionalInfo);
    }

    public Path generateEnhancedSummaryReport(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> reportData = repository.GetEnhancedSummaryData(startDate, endDate);
        Map<String, String> additionalInfo = new HashMap<>();
        additionalInfo.put("Period", startDate + " to " + endDate);
        return generateReport(reportData, "Enhanced Summary Report", "enhanced_summary_" + startDate + "_to_" + endDate + ".pdf", additionalInfo);
    }

    public Path generateStockValuationReportMeals(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> reportData = repository.GetStockValuationReportMeals(startDate, endDate);
        Map<String, String> additionalInfo = new HashMap<>();
        additionalInfo.put("Period", startDate + " to " + endDate);
        return generateReport(reportData, "Stock Valuation Report (Meals)", "stock_valuation_meals_" + startDate + "_to_" + endDate + ".pdf", additionalInfo);
    }

    public Path generateStockReportStores(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> reportData = repository.GetStockReportStores(startDate, endDate);
        Map<String, String> additionalInfo = new HashMap<>();
        additionalInfo.put("Period", startDate + " to " + endDate);
        return generateReport(reportData, "Stock Report (Stores)", "stock_report_stores_" + startDate + "_to_" + endDate + ".pdf", additionalInfo);
    }

    private Path generateReport(List<Map<String, Object>> reportData, String reportTitle, String fileName, Map<String, String> additionalInfo) {
        Path outputPath = Paths.get(System.getProperty("java.io.tmpdir"), fileName);
        reportGenerator.generateReport(reportData, reportTitle, outputPath.toString(), additionalInfo);
        return outputPath;
    }
}