package com.ctecx.brsuite.service;

public enum ReportType {
    INVOICE("Order"),
    RECEIPT("Receipt"),
    STATEMENT("PatientStatement");

    private final String fileName;

    ReportType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}