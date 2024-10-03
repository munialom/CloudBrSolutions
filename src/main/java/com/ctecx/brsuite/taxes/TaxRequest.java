package com.ctecx.brsuite.taxes;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class TaxRequest {
    private double amount;
    private List<Long> taxIds;
}