package com.ctecx.brsuite.stockmode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
@JsonPropertyOrder({
        "ProductID",
        "ProductName",
        "BuyPrice",
        "SalePrice",
        "Opening_Stock",
        "Added",
        "Subtracted",
        "Sales",
        "Current_Stock",
        "Gross_Sales",
        "Profit",
        "Closing_Stock"
})
public class StockReport {
    private Long productID;
    private String productName;
    private Double buyPrice;
    private Double salePrice;
    private BigDecimal opening_Stock;
    private BigDecimal added;
    private BigDecimal subtracted;
    private BigDecimal sales;
    private BigDecimal current_Stock;
    private Double gross_Sales;
    private Double profit;
    private BigDecimal closing_Stock;

    // Constructors...

    @JsonProperty("ProductID")
    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    @JsonProperty("ProductName")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @JsonProperty("BuyPrice")
    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    @JsonProperty("SalePrice")
    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    @JsonProperty("Opening_Stock")
    public BigDecimal getOpening_Stock() {
        return opening_Stock;
    }

    public void setOpening_Stock(BigDecimal opening_Stock) {
        this.opening_Stock = opening_Stock;
    }

    @JsonProperty("Added")
    public BigDecimal getAdded() {
        return added;
    }

    public void setAdded(BigDecimal added) {
        this.added = added;
    }

    @JsonProperty("Subtracted")
    public BigDecimal getSubtracted() {
        return subtracted;
    }

    public void setSubtracted(BigDecimal subtracted) {
        this.subtracted = subtracted;
    }

    @JsonProperty("Sales")
    public BigDecimal getSales() {
        return sales;
    }

    public void setSales(BigDecimal sales) {
        this.sales = sales;
    }

    @JsonProperty("Current_Stock")
    public BigDecimal getCurrent_Stock() {
        return current_Stock;
    }

    public void setCurrent_Stock(BigDecimal current_Stock) {
        this.current_Stock = current_Stock;
    }

    @JsonProperty("Gross_Sales")
    public Double getGross_Sales() {
        return gross_Sales;
    }

    public void setGross_Sales(Double gross_Sales) {
        this.gross_Sales = gross_Sales;
    }

    @JsonProperty("Profit")
    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    @JsonProperty("Closing_Stock")
    public BigDecimal getClosing_Stock() {
        return closing_Stock;
    }

    public void setClosing_Stock(BigDecimal closing_Stock) {
        this.closing_Stock = closing_Stock;
    }

    @Override
    public String toString() {
        return "StockReport{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", buyPrice=" + buyPrice +
                ", salePrice=" + salePrice +
                ", opening_Stock=" + opening_Stock +
                ", added=" + added +
                ", subtracted=" + subtracted +
                ", sales=" + sales +
                ", current_Stock=" + current_Stock +
                ", gross_Sales=" + gross_Sales +
                ", profit=" + profit +
                ", closing_Stock=" + closing_Stock +
                '}';
    }
}


