package com.ctecx.brsuite.stockmode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/stock-transactions")
public class StockTransactionController {

    private final StockTransactionService stockTransactionService;

    @Autowired
    public StockTransactionController(StockTransactionService stockTransactionService) {
        this.stockTransactionService = stockTransactionService;
    }

    @GetMapping
    public String getAllStockTransactions(Model model) {
        List<StockTransaction> stockTransactions = stockTransactionService.getAllStockTransactions();
        model.addAttribute("stockTransactions", stockTransactions);
        return "stockTransactionsList"; // Return the name of the view
    }

    @GetMapping("/{id}")
    public String getStockTransactionById(@PathVariable Long id, Model model) {
        StockTransaction stockTransaction = stockTransactionService.getStockTransactionById(id);
        if (stockTransaction != null) {
            model.addAttribute("stockTransaction", stockTransaction);
            return "stockTransactionDetails"; // Return the name of the view
        } else {
            return "notFound"; // Return the name of the view for not found
        }
    }

    @PostMapping
    public String createStockTransaction(@ModelAttribute StockTransaction stockTransaction) {
       stockTransactionService.createStockTransaction(stockTransaction);
        return "redirect:/"; // Redirect after creating
    }

    @DeleteMapping("/{id}")
    public String deleteStockTransaction(@PathVariable Long id) {
        stockTransactionService.deleteStockTransaction(id);
        return "redirect:/api/stock-transactions"; // Redirect after deleting
    }
}
