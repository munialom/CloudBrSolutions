package com.ctecx.brsuite.transactions;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleTransactionDTO {
    private String serialNumber;
    private BigDecimal totalAmount;
    private BigDecimal receivedAmount;
    private List<String> paymentModes;
    private String description;
}