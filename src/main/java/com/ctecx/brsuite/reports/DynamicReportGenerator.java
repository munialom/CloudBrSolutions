package com.ctecx.brsuite.reports;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class DynamicReportGenerator {
    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(52, 152, 219);
    private static final DeviceRgb ALTERNATE_ROW_COLOR = new DeviceRgb(240, 240, 240);
    private static final float DEFAULT_FONT_SIZE = 8f;
    private static final float TITLE_FONT_SIZE = 12f;
    private static final float COMPANY_FONT_SIZE = 10f;
    private static final float ADDRESS_FONT_SIZE = 8f;

    record ReportMetadata(String title, String companyName, String companyAddress, String telPhone, Map<String, String> additionalInfo) {}

    public Path generateReport(List<Map<String, Object>> reportData, String reportTitle, String outputFilename, Map<String, String> additionalInfo) {
        ReportMetadata metadata = new ReportMetadata(
                reportTitle,
                "PREMIER RESORT CHWELE",
                "OFF CHWELE -KIMILILI ROAD",
                "TEL +254718072087",
                additionalInfo
        );

        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
        Path outputPath = tempDir.resolve(outputFilename);

        try {
            List<String> orderedHeaders = determineColumnOrder(reportData);
            createPDF(outputPath.toString(), reportData, orderedHeaders, metadata);
            System.out.println("PDF report generated successfully: " + outputPath);
            return outputPath;
        } catch (IOException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to generate report", e);
        }
    }

    private List<String> determineColumnOrder(List<Map<String, Object>> data) {
        if (data.isEmpty()) {
            return new ArrayList<>();
        }
        return data.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .map(row -> new ArrayList<>(row.keySet()))
                .orElse(new ArrayList<>());
    }

    private void createPDF(String filename, List<Map<String, Object>> data, List<String> orderedHeaders, ReportMetadata metadata) throws IOException {
        PageSize pageSize = PageSize.A4.rotate();

        try (var writer = new PdfWriter(filename);
             var pdf = new PdfDocument(writer);
             var document = new Document(pdf, pageSize)) {

            document.setMargins(70, 20, 25, 20);
            pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new HeaderFooterEventHandler(metadata));

            Table table = createTable(data, orderedHeaders);
            document.add(table);
        }
    }

    private Table createTable(List<Map<String, Object>> data, List<String> headers) {
        float[] columnWidths = calculateColumnWidths(headers, data);
        Table table = new Table(UnitValue.createPointArray(columnWidths)).useAllAvailableWidth();

        Map<String, ColumnType> columnTypes = determineColumnTypes(headers, data);

        headers.forEach(header -> {
            Cell cell = new Cell().add(new Paragraph(header).setFontSize(DEFAULT_FONT_SIZE))
                    .setBackgroundColor(HEADER_COLOR)
                    .setFontColor(ColorConstants.WHITE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(2);
            table.addHeaderCell(cell);
        });

        IntStream.range(0, data.size())
                .forEach(i -> addRowToTable(table, data.get(i), headers, columnTypes, i % 2 == 1));

        return table;
    }

    private float[] calculateColumnWidths(List<String> headers, List<Map<String, Object>> data) {
        Map<String, Integer> maxLengths = new HashMap<>();
        for (String header : headers) {
            maxLengths.put(header, header.length());
        }

        for (Map<String, Object> row : data) {
            for (String header : headers) {
                Object value = row.get(header);
                int length = value == null ? 0 : value.toString().length();
                maxLengths.put(header, Math.max(maxLengths.get(header), length));
            }
        }

        float[] widths = new float[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            widths[i] = Math.max(20f, Math.min(100f, maxLengths.get(header) * 5f));
        }
        return widths;
    }

    private enum ColumnType {
        ID, NUMERIC, TEXT
    }

    private Map<String, ColumnType> determineColumnTypes(List<String> headers, List<Map<String, Object>> data) {
        Map<String, ColumnType> columnTypes = new HashMap<>();

        for (String header : headers) {
            ColumnType type = ColumnType.TEXT;
            boolean allNumeric = true;
            boolean allId = true;

            for (Map<String, Object> row : data) {
                Object value = row.get(header);
                if (value != null) {
                    String strValue = value.toString().trim();
                    if (!isNumeric(strValue)) {
                        allNumeric = false;
                    }
                    if (!strValue.matches("\\d+")) {
                        allId = false;
                    }
                }
            }

            if (allId && header.toLowerCase().matches(".*(id|no|code).*")) {
                type = ColumnType.ID;
            } else if (allNumeric) {
                type = ColumnType.NUMERIC;
            }

            columnTypes.put(header, type);
        }

        return columnTypes;
    }

    private boolean isNumeric(String strValue) {
        if (strValue.matches("-?\\d+(\\.\\d+)?")) {
            return true;
        }
        // Check for numbers with thousand separators
        if (strValue.matches("-?\\d{1,3}(,\\d{3})*(\\.\\d+)?")) {
            try {
                DecimalFormat df = new DecimalFormat("#,##0.00");
                df.setParseBigDecimal(true);
                df.parse(strValue);
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }

    private void addRowToTable(Table table, Map<String, Object> row, List<String> headers,
                               Map<String, ColumnType> columnTypes, boolean alternate) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        headers.forEach(header -> {
            Object value = row.get(header);
            String cellValue;
            TextAlignment alignment;

            ColumnType columnType = columnTypes.get(header);

            switch (columnType) {
                case ID:
                    cellValue = value != null ? String.valueOf(value) : "";
                    alignment = TextAlignment.CENTER;
                    break;
                case NUMERIC:
                    if (value == null) {
                        cellValue = "";
                    } else if (value instanceof Number) {
                        cellValue = decimalFormat.format(((Number) value).doubleValue());
                    } else {
                        String strValue = value.toString().trim();
                        try {
                            // Parse the string value and format it consistently
                            Number parsedValue = decimalFormat.parse(strValue);
                            cellValue = decimalFormat.format(parsedValue);
                        } catch (ParseException e) {
                            // If parsing fails, use the original string value
                            cellValue = strValue;
                        }
                    }
                    alignment = TextAlignment.RIGHT;
                    break;
                default: // TEXT
                    cellValue = value != null ? String.valueOf(value) : "";
                    alignment = TextAlignment.LEFT;
            }

            Cell cell = new Cell().add(new Paragraph(cellValue).setFontSize(DEFAULT_FONT_SIZE))
                    .setTextAlignment(alignment)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(2);

            if (alternate) {
                cell.setBackgroundColor(ALTERNATE_ROW_COLOR);
            }

            table.addCell(cell);
        });
    }

    private static class HeaderFooterEventHandler implements IEventHandler {
        private final ReportMetadata metadata;

        public HeaderFooterEventHandler(ReportMetadata metadata) {
            this.metadata = metadata;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            Rectangle pageSize = page.getPageSize();

            try (var canvas = new Canvas(new PdfCanvas(page), pageSize)) {
                // Header
                float yOffset = pageSize.getTop() - 20;
                canvas.showTextAligned(new Paragraph(metadata.title()).setFontSize(TITLE_FONT_SIZE).setBold(),
                        pageSize.getWidth() / 2, yOffset, TextAlignment.CENTER);
                yOffset -= 15;
                canvas.showTextAligned(new Paragraph(metadata.companyName()).setFontSize(COMPANY_FONT_SIZE),
                        pageSize.getWidth() / 2, yOffset, TextAlignment.CENTER);
                yOffset -= 12;
                canvas.showTextAligned(new Paragraph(metadata.companyAddress()).setFontSize(ADDRESS_FONT_SIZE),
                        pageSize.getWidth() / 2, yOffset, TextAlignment.CENTER);

                // Additional Info
                for (Map.Entry<String, String> entry : metadata.additionalInfo().entrySet()) {
                    yOffset -= 12;
                    canvas.showTextAligned(new Paragraph(entry.getKey() + ": " + entry.getValue()).setFontSize(ADDRESS_FONT_SIZE),
                            pageSize.getWidth() / 2, yOffset, TextAlignment.CENTER);
                }

                // Footer
                float footerY = 20;
                float leftMargin = 20;
                float rightMargin = pageSize.getWidth() - 20;

                // Checked By and Approved By
                canvas.showTextAligned(new Paragraph("Checked By: ________________").setFontSize(DEFAULT_FONT_SIZE),
                        leftMargin, footerY, TextAlignment.LEFT);
                canvas.showTextAligned(new Paragraph("Approved By: ________________").setFontSize(DEFAULT_FONT_SIZE),
                        rightMargin, footerY, TextAlignment.RIGHT);

                footerY -= 10;
                String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                canvas.showTextAligned(new Paragraph("Generated on: " + currentTime).setFontSize(DEFAULT_FONT_SIZE),
                        leftMargin, footerY, TextAlignment.LEFT);

                int currentPageNumber = pdf.getPageNumber(page);
                int totalPages = pdf.getNumberOfPages();
                canvas.showTextAligned(new Paragraph(String.format("Page %d of %d", currentPageNumber, totalPages)).setFontSize(DEFAULT_FONT_SIZE),
                        rightMargin, footerY, TextAlignment.RIGHT);
            }
        }
    }
}