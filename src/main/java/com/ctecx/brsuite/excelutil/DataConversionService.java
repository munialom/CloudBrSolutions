package com.ctecx.brsuite.excelutil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataConversionService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public byte[] generateExcelFromJson(String jsonData) throws IOException {
        List<LinkedHashMap<String, Object>> dataList = parseJsonData(jsonData);
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");
            List<String> headers = new ArrayList<>(dataList.get(0).keySet());

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle lastRowStyle = createLastRowStyle(workbook);

            createHeaderRow(sheet, headers, headerStyle);
            populateDataRows(sheet, dataList, headers, dataStyle, lastRowStyle);
            autoSizeColumns(sheet, headers.size());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] generateCsvFromJson(String jsonData) throws IOException {
        List<LinkedHashMap<String, Object>> dataList = parseJsonData(jsonData);
        List<String> headers = new ArrayList<>(dataList.get(0).keySet());
        StringBuilder csvContent = new StringBuilder();

        // Add headers
        csvContent.append(String.join(",", headers)).append("\n");

        // Add data rows
        for (Map<String, Object> data : dataList) {
            List<String> rowData = new ArrayList<>();
            for (String header : headers) {
                rowData.add(data.getOrDefault(header, "").toString().replace(",", ";")); // Replace commas to avoid CSV issues
            }
            csvContent.append(String.join(",", rowData)).append("\n");
        }

        return csvContent.toString().getBytes();
    }

    private List<LinkedHashMap<String, Object>> parseJsonData(String jsonData) throws IOException {
        if (jsonData.trim().startsWith("[")) {
            return objectMapper.readValue(jsonData, new TypeReference<List<LinkedHashMap<String, Object>>>() {});
        } else if (jsonData.trim().startsWith("{")) {
            List<LinkedHashMap<String, Object>> list = new ArrayList<>();
            list.add(objectMapper.readValue(jsonData, new TypeReference<LinkedHashMap<String, Object>>() {}));
            return list;
        } else {
            throw new IllegalArgumentException("Invalid JSON format");
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        setBorders(style);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setBorders(style);
        return style;
    }

    private CellStyle createLastRowStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        setBorders(style);
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private void createHeaderRow(Sheet sheet, List<String> headers, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerStyle);
        }
    }

    private void populateDataRows(Sheet sheet, List<LinkedHashMap<String, Object>> dataList, List<String> headers, CellStyle dataStyle, CellStyle lastRowStyle) {
        for (int i = 0; i < dataList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Map<String, Object> data = dataList.get(i);
            CellStyle rowStyle = (i == dataList.size() - 1) ? lastRowStyle : dataStyle;

            for (int j = 0; j < headers.size(); j++) {
                Cell cell = row.createCell(j);
                Object value = data.get(headers.get(j));
                cell.setCellValue(value != null ? value.toString() : "");
                cell.setCellStyle(rowStyle);
            }
        }
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}