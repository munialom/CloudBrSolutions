package com.ctecx.brsuite.chartofaccounts;

import lombok.Data;

@Data
public class AccountChartDTO {
    private String name;
    private String alias;
    private AccountGroup accountGroup;
    private Integer parentId;
    private boolean bankAccount;
    private Integer linkedBankAccountId;
    private String currencyCode;
}