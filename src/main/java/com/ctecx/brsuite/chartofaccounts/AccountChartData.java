package com.ctecx.brsuite.chartofaccounts;

public class AccountChartData {
    private Integer id;
    private String name;

    public AccountChartData(Integer id, String name, Integer accountCode) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}