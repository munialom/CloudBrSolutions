package com.ctecx.brsuite.purchases;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuanceStockDTO {
    private Long fromStoreId;
    private Long toStoreId;
    private LocalDate issuanceDate;
    private List<IssuanceStock> issuanceStocks;
    private String description;
}