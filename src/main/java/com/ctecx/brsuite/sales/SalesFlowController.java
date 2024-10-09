package com.ctecx.brsuite.sales;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("sales")
@AllArgsConstructor
public class SalesFlowController {




    @GetMapping("/open-sales")
    public String openOrders() {

        return "waiters/openOrders"; // This corresponds to the Thymeleaf HTML template
    }

    @GetMapping("/sale-controls")
    public String controlPanel() {

        return "sales/salesControlPanel"; // This corresponds to the Thymeleaf HTML template
    }


    @GetMapping("/all-sales-reports")
    public String allReports() {

        return "reports/general-reports"; // This corresponds to the Thymeleaf HTML template
    }


    @GetMapping("/daily-aggregates")
    public String dailyAggregates() {

        return "reports/aggregate-monthly-sales"; // This corresponds to the Thymeleaf HTML template
    }

    @GetMapping("/acc-aggregates")
    public String AccountAggregates() {

        return "reports/monthly-sales-by-Revenue"; // This corresponds to the Thymeleaf HTML template
    }

    @GetMapping("/yr-aggregates")
    public String AccountYearlyAggregates() {

        return "reports/yearly-sales-by-Revenue"; // This corresponds to the Thymeleaf HTML template
    }


    @GetMapping("stock-sheet")
    public String stockSheet(Model model) {

        return "reports/new-stock-sheet-data";
    }

}
