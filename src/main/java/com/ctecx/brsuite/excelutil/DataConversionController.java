package com.ctecx.brsuite.excelutil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataConversionController {

    @Autowired
    private DataConversionService dataConversionService;

    @PostMapping("/generate-excel")
    public ResponseEntity<byte[]> generateExcel(@RequestBody ConversionRequest request) {
        try {
            byte[] excelBytes = dataConversionService.generateExcelFromJson(request.getJsonData());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", request.getFilename() + ".xlsx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/generate-csv")
    public ResponseEntity<byte[]> generateCsv(@RequestBody ConversionRequest request) {
        try {
            byte[] csvBytes = dataConversionService.generateCsvFromJson(request.getJsonData());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", request.getFilename() + ".csv");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}