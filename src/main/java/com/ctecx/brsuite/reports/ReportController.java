package com.ctecx.brsuite.reports;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {


    private final ReportService reportService;

    @GetMapping("/stock-valuation")
    public ResponseEntity<Resource> generateStockValuationReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "open") String action) throws IOException {
        return servePdf(() -> reportService.generateStockValuationReport(startDate, endDate), action);
    }

    @GetMapping("/revenue-summary")
    public ResponseEntity<Resource> generateRevenueSummaryReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate,
            @RequestParam(defaultValue = "open") String action) throws IOException {
        return servePdf(() -> reportService.generateRevenueSummaryReport(localDate), action);
    }

    @GetMapping("/summary-transaction")
    public ResponseEntity<Resource> generateSummaryTransactionReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "open") String action) throws IOException {
        return servePdf(() -> reportService.generateSummaryTransactionReport(startDate, endDate), action);
    }

    @GetMapping("/waiters-summary")
    public ResponseEntity<Resource> generateWaitersSummaryReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate,
            @RequestParam(defaultValue = "open") String action) throws IOException {
        return servePdf(() -> reportService.generateWaitersSummaryReport(localDate), action);
    }

    @GetMapping("/enhanced-sales")
    public ResponseEntity<Resource> generateEnhancedSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "open") String action) throws IOException {
        return servePdf(() -> reportService.generateEnhancedSalesReport(startDate, endDate), action);
    }

    @GetMapping("/enhanced-sales-waiter")
    public ResponseEntity<Resource> generateEnhancedSalesReportWaiter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String waiterName,
            @RequestParam(defaultValue = "open") String action) throws IOException {
        return servePdf(() -> reportService.generateEnhancedSalesReportWaiter(startDate, endDate, waiterName), action);
    }

    @GetMapping("/enhanced-summary")
    public ResponseEntity<Resource> generateEnhancedSummaryReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "open") String action) throws IOException {
        return servePdf(() -> reportService.generateEnhancedSummaryReport(startDate, endDate), action);
    }

    @GetMapping("/stock-valuation-meals")
    public ResponseEntity<Resource> generateStockValuationReportMeals(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "open") String action) throws IOException {
        return servePdf(() -> reportService.generateStockValuationReportMeals(startDate, endDate), action);
    }

    @GetMapping("/stock-report-stores")
    public ResponseEntity<Resource> generateStockReportStores(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "open") String action) throws IOException {
        return servePdf(() -> reportService.generateStockReportStores(startDate, endDate), action);
    }

    // Helper method for serving PDF reports
    private ResponseEntity<Resource> servePdf(Supplier<Path> reportGenerator, String action) throws IOException {
        Path reportPath = reportGenerator.get();
        Resource resource = loadFileAsResource(reportPath);
        String fileName = reportPath.getFileName().toString();
        String contentDisposition = action.equalsIgnoreCase("download")
                ? "attachment; filename=\"" + fileName + "\""
                : "inline; filename=\"" + fileName + "\"";
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    private Resource loadFileAsResource(Path filePath) throws IOException {
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new IOException("File not found: " + filePath);
            }
        } catch (MalformedURLException ex) {
            throw new IOException("File not found: " + filePath, ex);
        }
    }
}