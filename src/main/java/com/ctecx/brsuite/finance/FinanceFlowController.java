package com.ctecx.brsuite.finance;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("finance")
@AllArgsConstructor
public class FinanceFlowController {


    @GetMapping("revenueSummary")
    public String Workflow(Model model) {

        return "reports/revenue-summary";
    }



    @GetMapping("byRevenueCodes")
    public String WorkflowReports(Model model) {

        return "reports/revenue-codes";
    }

    @GetMapping("payment-methods")
    public String paymentMethods(Model model) {

        return "reports/payment-methods";
    }

    @GetMapping("waiter-summary")
    public String waiterData(Model model) {

        return "reports/waiter-summary";
    }


    @GetMapping("products-summary")
    public String productsData(Model model) {

        return "reports/products-summary";
    }

    @GetMapping("products-sales-summary")
    public String updatedData(Model model) {

        return "reports/products-sales-summary";
    }

    @GetMapping("stock-sheet")
    public String stockSheet(Model model) {

        return "reports/new-stock-sheet-data";
    }


    @GetMapping("gas-stock-sheet")
    public String GasstockSheet(Model model) {

        return "reports/gas-stock-sheet-data";
    }




    @GetMapping("stock-sheet-meals")
    public String stockSheetMeals(Model model) {

        return "reports/stock-sheet-meals";
    }


    @GetMapping("sales-dashboard")
    public String salesDashboard(Model model) {

        return "reports/sales-dashboard";
    }

    @GetMapping("stores")
    public String storesData(Model model) {

        return "reports/stock-sheet-main-store";
    }










}
