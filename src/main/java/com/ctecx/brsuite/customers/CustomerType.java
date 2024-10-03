package com.ctecx.brsuite.customers;

public enum CustomerType {
    COMPANY("Company"),
    INDIVIDUAL("Individual");

    private final String displayText;

    CustomerType(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
