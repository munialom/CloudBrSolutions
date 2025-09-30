package com.ctecx.brsuite.customproductsmanager;

import lombok.Data;

@Data
public class PrintStatusUpdateRequest {
    private String type; // "ORDER" or "RECEIPT"
    private String identifier;
    private boolean status; // true for printed, false for not printed
}