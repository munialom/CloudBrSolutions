package com.ctecx.brsuite.products;

import java.util.UUID;

public class ProductCodeGenerator {
    public static String generateProductCode() {
        UUID uuid = UUID.randomUUID();
        String productCode = uuid.toString().substring(0, 8).toUpperCase();
        return productCode;
    }
}
