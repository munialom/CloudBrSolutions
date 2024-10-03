package com.ctecx.brsuite.purchases;

import lombok.Data;

import java.util.List;
@Data
public class OpeningStockDTO {
    private List<OpeningStock> openingStocks;
    private String opcode;
    private Long branchId;  // Add this field
}
