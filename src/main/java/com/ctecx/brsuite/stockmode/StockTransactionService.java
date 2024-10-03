package com.ctecx.brsuite.stockmode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockTransactionService {

    private final StockTransactionRepository stockTransactionRepository;

    @Autowired
    public StockTransactionService(StockTransactionRepository stockTransactionRepository) {
        this.stockTransactionRepository = stockTransactionRepository;
    }

    public List<StockTransaction> getAllStockTransactions() {
        return stockTransactionRepository.findAll();
    }

    public StockTransaction getStockTransactionById(Long id) {
        return stockTransactionRepository.findById(id).orElse(null);
    }

    public StockTransaction createStockTransaction(StockTransaction stockTransaction) {
        return stockTransactionRepository.save(stockTransaction);
    }


   public StockTransactionDTO getStockTransaction(String serialNumber) {
       List<StockTransaction> transactions = stockTransactionRepository.findBySerialNumber(serialNumber);
       if (transactions == null || transactions.isEmpty()) {
           throw new RuntimeException("Transactions not found for serial number: " + serialNumber);
       }
       StockTransaction transaction = transactions.get(0);

       StockTransactionDTO stockTransactionDto = new StockTransactionDTO();
       stockTransactionDto.setProductCode(transaction.getProductCode());
       stockTransactionDto.setDiscount(transaction.getDiscount());
       stockTransactionDto.setTax(transaction.getTax());
       stockTransactionDto.setNetTax(transaction.getNetTax());
       stockTransactionDto.setSubtotal(transaction.getSubtotal());
       stockTransactionDto.setTransactionDate(transaction.getTransactionDate());
       stockTransactionDto.setModule(transaction.getModule());
       stockTransactionDto.setSerialNumber(transaction.getSerialNumber());
       stockTransactionDto.setCustomerName(transaction.getCustomer().getCustomername());
       stockTransactionDto.setDescription(transaction.getDescription());
       stockTransactionDto.setStatus(transaction.getStatus());
       stockTransactionDto.setSubModule(transaction.getSubModule());
       stockTransactionDto.setCreatedBy(transaction.getCreatedBy());
       stockTransactionDto.setLocalDateTime(transaction.getCreated());
       Map<String, List<ProductDetail>> productDetailsMap = new HashMap<>();
       BigDecimal total = BigDecimal.ZERO;
       BigDecimal subtotal = BigDecimal.ZERO;
       BigDecimal netTax = BigDecimal.ZERO;
       BigDecimal changeOut = null; // Initialize changeOut as null

       for (StockTransaction trx : transactions) {
           String productCode = trx.getProductCode();
           double productCost = trx.getProductCost();
           double productSalePrice = trx.getProductSalePrice();
           Integer stockIn = trx.getStockIn();
           Integer stockOut = trx.getStockOut();
           String productName = trx.getProductName();
           BigDecimal trxNetTax = trx.getNetTax();
           BigDecimal trxSubtotal = trx.getSubtotal();
           BigDecimal trxChangeOut = trx.getChangeOut();

           // Set changeOut to the first non-null value encountered
           if (changeOut == null && trxChangeOut != null) {
               changeOut = trxChangeOut;
           }

           ProductDetail productDetail = new ProductDetail(productCost, productSalePrice, stockIn, stockOut, productName, trxNetTax, trxSubtotal);
           if (!productDetailsMap.containsKey(productCode)) {
               productDetailsMap.put(productCode, new ArrayList<>());
           }
           productDetailsMap.get(productCode).add(productDetail);

           // Add netTax and subtotal of each transaction to the respective totals
           if (trxNetTax != null) {
               total = total.add(trxNetTax);
               netTax = netTax.add(trxNetTax);
           }
           if (trxSubtotal != null) {
               total = total.add(trxSubtotal);
               subtotal = subtotal.add(trxSubtotal);
           }
       }

       stockTransactionDto.setProductDetailsMap(productDetailsMap);
       stockTransactionDto.setTotal(total);
       stockTransactionDto.setSubtotal(subtotal);
       stockTransactionDto.setNetTax(netTax);
       stockTransactionDto.setChangeOut(changeOut); // Set the first non-null value of changeOut
       stockTransactionDto.setAmountPaid(subtotal.add(changeOut != null ? changeOut : BigDecimal.ZERO).add(netTax));
       return stockTransactionDto;
   }


    public StockTransactionDTO getStockTransactionByOrderNumber(String serialNumber) {
        List<StockTransaction> transactions = stockTransactionRepository.findByOrderNumber(serialNumber);
        if (transactions == null || transactions.isEmpty()) {
            throw new RuntimeException("Transactions not found for serial number: " + serialNumber);
        }
        StockTransaction transaction = transactions.get(0);
        StockTransactionDTO stockTransactionDto = new StockTransactionDTO();
        stockTransactionDto.setProductCode(transaction.getProductCode());
        stockTransactionDto.setReceiptNumber(serialNumber, true);
        stockTransactionDto.setDiscount(transaction.getDiscount());
        stockTransactionDto.setTax(transaction.getTax());
        stockTransactionDto.setNetTax(transaction.getNetTax());
        stockTransactionDto.setSubtotal(transaction.getSubtotal());
        stockTransactionDto.setTransactionDate(transaction.getTransactionDate());
        stockTransactionDto.setModule(transaction.getModule());
        stockTransactionDto.setSerialNumber(transaction.getSerialNumber());
        stockTransactionDto.setCustomerName(transaction.getCustomer().getCustomername());
        stockTransactionDto.setDescription(transaction.getDescription());
        stockTransactionDto.setStatus(transaction.getStatus());
        stockTransactionDto.setSubModule(transaction.getSubModule());
        stockTransactionDto.setCreatedBy(transaction.getCreatedBy());
        stockTransactionDto.setLocalDateTime(transaction.getCreated());
        Map<String, List<ProductDetail>> productDetailsMap = new HashMap<>();
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal netTax = BigDecimal.ZERO;
        BigDecimal changeOut = null; // Initialize changeOut as null

        for (StockTransaction trx : transactions) {
            String productCode = trx.getProductCode();
            double productCost = trx.getProductCost();
            double productSalePrice = trx.getProductSalePrice();
            Integer stockIn = trx.getStockIn();
            Integer stockOut = trx.getStockOut();
            String productName = trx.getProductName();
            BigDecimal trxNetTax = trx.getNetTax();
            BigDecimal trxSubtotal = trx.getSubtotal();
            BigDecimal trxChangeOut = trx.getChangeOut();

            // Set changeOut to the first non-null value encountered
            if (changeOut == null && trxChangeOut != null) {
                changeOut = trxChangeOut;
            }

            ProductDetail productDetail = new ProductDetail(productCost, productSalePrice, stockIn, stockOut, productName, trxNetTax, trxSubtotal);
            if (!productDetailsMap.containsKey(productCode)) {
                productDetailsMap.put(productCode, new ArrayList<>());
            }
            productDetailsMap.get(productCode).add(productDetail);

            // Add netTax and subtotal of each transaction to the respective totals
            if (trxNetTax != null) {
                total = total.add(trxNetTax);
                netTax = netTax.add(trxNetTax);
            }
            if (trxSubtotal != null) {
                total = total.add(trxSubtotal);
                subtotal = subtotal.add(trxSubtotal);
            }
        }

        stockTransactionDto.setProductDetailsMap(productDetailsMap);
        stockTransactionDto.setTotal(total);
        stockTransactionDto.setSubtotal(subtotal);
        stockTransactionDto.setNetTax(netTax);
        stockTransactionDto.setChangeOut(changeOut); // Set the first non-null value of changeOut
        stockTransactionDto.setAmountPaid(subtotal.add(changeOut != null ? changeOut : BigDecimal.ZERO).add(netTax));
        return stockTransactionDto;
    }


    public void deleteStockTransaction(Long id) {
        stockTransactionRepository.deleteById(id);
    }
}
