package com.ctecx.brsuite.stockmode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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





    public void deleteStockTransaction(Long id) {
        stockTransactionRepository.deleteById(id);
    }
}
