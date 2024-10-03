package com.ctecx.brsuite.purchases;

import lombok.Data;

import java.util.List;
@Data
public class StoreOpeningStockDTO {
    private List<StoreOpeningStock> storeOpeningStocks;
    private String opcode;
    private Long branchId;  // Add this field
}
