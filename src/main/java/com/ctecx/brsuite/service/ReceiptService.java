package com.ctecx.brsuite.service;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReceiptService {
    private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);
    private static final String REPORTS_PATH = "reports/";

    @Autowired
    private DataSource dataSource;

    public JasperPrint generateReport(ReportType reportType, Map<String, Object> parameters) {
        try {
            String reportPath = REPORTS_PATH + reportType.getFileName() + ".jasper";
            logger.info("Loading report from classpath: {}", reportPath);

            // Load report from classpath resources
            try (InputStream reportStream = getClass().getClassLoader().getResourceAsStream(reportPath)) {
                if (reportStream == null) {
                    throw new RuntimeException("Report template not found in classpath: " + reportPath);
                }

                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

                // Initialize parameters if null
                if (parameters == null) {
                    parameters = new HashMap<>();
                }

                return JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
            }
        } catch (Exception e) {
            logger.error("Failed to generate report: {}", reportType, e);
            throw new RuntimeException("Failed to generate report: " + reportType.getFileName(), e);
        }
    }

    public byte[] exportToPdf(JasperPrint jasperPrint, String author, boolean encrypted) {
        try {
            JRPdfExporter exporter = new JRPdfExporter();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Set input and output
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            // Configure PDF report settings
            SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
            reportConfig.setSizePageToContent(true);
            reportConfig.setForceLineBreakPolicy(false);
            exporter.setConfiguration(reportConfig);

            // Configure PDF export settings
            SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
            exportConfig.setMetadataAuthor(author);
            exportConfig.setEncrypted(encrypted);
            if (encrypted) {
                exportConfig.setAllowedPermissionsHint("PRINTING");
            }
            exporter.setConfiguration(exportConfig);

            // Perform export
            exporter.exportReport();

            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("Failed to export report to PDF", e);
            throw new RuntimeException("Failed to export report to PDF", e);
        }
    }
}