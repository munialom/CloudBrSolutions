package com.ctecx.brsuite.stockmode;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class SalesStockDTO {
    List<SaleStock> saleStocks;
    private Long branchId;
    private LocalDate salesDate;

    private List<String> payMode;

    private Long customerId;
    private BigDecimal amountPaid;
    private BigDecimal totalAmount;
    private BigDecimal changeOut;
    private String transactionType;
    private boolean addItems;
    private String existingSerialNumber;

}
