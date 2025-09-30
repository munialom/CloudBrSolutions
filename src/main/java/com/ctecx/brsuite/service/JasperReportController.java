package com.ctecx.brsuite.service;

import com.ctecx.brsuite.customproductsmanager.CustomManagerProductService;
import com.ctecx.brsuite.customproductsmanager.PrintStatusUpdateRequest;
import com.ctecx.brsuite.systemsetup.AppSetupService;
import com.ctecx.brsuite.util.SecurityUtils; // Assuming SecurityUtils is available
import net.sf.jasperreports.engine.JasperPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/jasper")
public class JasperReportController {
    private static final Logger logger = LoggerFactory.getLogger(JasperReportController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private AppSetupService appSetupService;
    @Autowired
    private CustomManagerProductService customManagerProductService;

    @PostMapping("/receipt/{receiptNumber}")
    public ResponseEntity<?> generateReceipt(@PathVariable String receiptNumber, @RequestParam(required = false) String filename) {
        if (!isPrintAllowed("RECEIPT", receiptNumber)) {
            logger.warn("Unauthorized re-print attempt for RECEIPT {} by user '{}'", receiptNumber, getCurrentUsername());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "This receipt has already been printed. Re-printing requires administrator privileges."));
        }

        Map<String, Object> dynamicSettings = appSetupService.getLimitedSchoolSettingsMap();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("receiptNumber", receiptNumber);
        parameters.put("hospitalName", getBranchName());
        parameters.put("hospitalAddress", dynamicSettings.getOrDefault("SCHOOL_POSTAL", "N/A"));
        parameters.put("hospitalContact", dynamicSettings.getOrDefault("SCHOOL_PHONE", "N/A"));
        parameters.put("KRA_TAX-PIN", dynamicSettings.getOrDefault("SCHOOL_KRA", "N/A"));
        String actualFilename = (filename != null && !filename.isEmpty()) ? filename : generateDefaultFilename("cash_sale_receipt");
        logger.info("Generating cash sale receipt report with filename: {} for receipt: {}", actualFilename, receiptNumber);

        return generateReportResponse(ReportType.RECEIPT, parameters, actualFilename);
    }

    @PostMapping("/order/{orderNumber}")
    public ResponseEntity<?> generateOrder(@PathVariable String orderNumber, @RequestParam(required = false) String filename) {
        if (!isPrintAllowed("ORDER", orderNumber)) {
            logger.warn("Unauthorized re-print attempt for ORDER {} by user '{}'", orderNumber, getCurrentUsername());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "This order has already been printed. Re-printing requires administrator privileges."));
        }

        Map<String, Object> dynamicSettings = appSetupService.getLimitedSchoolSettingsMap();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("receiptNumber", orderNumber);
        parameters.put("hospitalName", getBranchName());
        parameters.put("hospitalAddress", dynamicSettings.getOrDefault("SCHOOL_POSTAL", "N/A"));
        parameters.put("hospitalContact", dynamicSettings.getOrDefault("SCHOOL_PHONE", "N/A"));
        String actualFilename = (filename != null && !filename.isEmpty()) ? filename : generateDefaultFilename("kitchen_order");
        logger.info("Generating kitchen order report with filename: {} for order: {}", actualFilename, orderNumber);

        return generateReportResponse(ReportType.INVOICE, parameters, actualFilename);
    }

    /**
     * Retrieves the branch name based on the current user's branch ID.
     */
    private String getBranchName() {
        try {
            Long branchId = SecurityUtils.getCurrentUserBranch().getId();
            switch (branchId.intValue()) {
                case 18:
                    return "BreakFast Masters-MiniMart";
                case 19:
                    return "G2 Lounge-Kanduyi";
                case 20:
                    return "BreakFast Masters-Hotel";
                default:
                    logger.warn("Unknown branch ID: {}. Defaulting to 'N/A'", branchId);
                    return "N/A";
            }
        } catch (Exception e) {
            logger.error("Failed to retrieve branch ID: {}", e.getMessage(), e);
            return "N/A";
        }
    }

    /**
     * Checks if the current user is allowed to print the item.
     */
    private boolean isPrintAllowed(String type, String identifier) {
        boolean hasBeenPrinted = customManagerProductService.checkPrintStatus(type, identifier).orElse(false);
        if (!hasBeenPrinted) {
            return true;
        }
        if (isCurrentUserAdmin()) {
            logger.info("Admin user '{}' is re-printing a document of type {} with ID {}", getCurrentUsername(), type, identifier);
            return true;
        }
        return false;
    }

    /**
     * Helper method to check if the current user has the Admin role.
     */
    private boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> "ROLE_Admin".equals(grantedAuthority.getAuthority()));
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication.getName() : "anonymous";
    }

    private ResponseEntity<byte[]> generateReportResponse(
            ReportType reportType,
            Map<String, Object> parameters,
            String filename) {
        try {
            logger.debug("Starting report generation for type: {}", reportType);
            JasperPrint jasperPrint = receiptService.generateReport(reportType, parameters);
            byte[] pdfBytes = receiptService.exportToPdf(jasperPrint, "CloudHIMS", true);

            updatePrintStatus(reportType, (String) parameters.get("receiptNumber"));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", ensurePdfExtension(filename));
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            logger.info("Successfully generated {} report: {}", reportType, filename);
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            logger.error("Failed to generate {} report: {}", reportType, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(("Failed to generate report: " + e.getMessage()).getBytes());
        }
    }

    private void updatePrintStatus(ReportType reportType, String identifier) {
        String typeString;
        if (reportType == ReportType.RECEIPT) {
            typeString = "RECEIPT";
        } else if (reportType == ReportType.INVOICE) {
            typeString = "ORDER";
        } else {
            return;
        }

        try {
            logger.info("Attempting to update print status for {} with identifier: {}", typeString, identifier);
            PrintStatusUpdateRequest updateRequest = new PrintStatusUpdateRequest();
            updateRequest.setType(typeString);
            updateRequest.setIdentifier(identifier);
            updateRequest.setStatus(true);
            int updatedRows = customManagerProductService.updatePrintStatus(updateRequest);
            if (updatedRows > 0) {
                logger.info("Successfully updated print status for {} {}", typeString, identifier);
            } else {
                logger.warn("Print status update for {} {} affected 0 rows. Identifier might not exist.", typeString, identifier);
            }
        } catch (Exception e) {
            logger.error("Failed to update print status for {} {}: {}", typeString, identifier, e.getMessage(), e);
        }
    }

    private String generateDefaultFilename(String prefix) {
        return prefix + "_" + LocalDateTime.now().format(DATE_FORMATTER) + ".pdf";
    }

    private String ensurePdfExtension(String filename) {
        if (!filename.toLowerCase().endsWith(".pdf")) {
            filename += ".pdf";
        }
        return filename;
    }
}