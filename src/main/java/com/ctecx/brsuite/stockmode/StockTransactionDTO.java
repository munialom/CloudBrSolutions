package com.ctecx.brsuite.stockmode;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
public class StockTransactionDTO {

    private Map<String, List<ProductDetail>> productDetailsMap;
    private String batchNumber;
    private LocalDate expiryDate;
    private BigDecimal discount;
    private BigDecimal tax;
    private String productCode;
    private BigDecimal netTax;
    private BigDecimal subtotal;
    private LocalDate transactionDate;
    private String module;
    private String serialNumber;
    private String description;
    private String status;
    private String subModule;
    private String createdBy;
    private LocalDateTime localDateTime;
    private BigDecimal total;
    private BigDecimal changeOut;
    private BigDecimal amountPaid;
    private String customerName;
    private String orderNumber;
    private String receiptNumber;

    public void setReceiptNumber(String number, boolean isOrderNumber) {
        this.receiptNumber = number;
        if (isOrderNumber) {
            this.orderNumber = number;
        } else {
            this.serialNumber = number;
        }
    }

}

