package com.ctecx.brsuite.requisitions;

import lombok.Getter;

@Getter
public enum RequisitionStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    PARTIALLY_FULFILLED("Partially Fulfilled"),
    FULFILLED("Fulfilled"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled");

    private final String displayName;

    RequisitionStatus(String displayName) {
        this.displayName = displayName;
    }
}