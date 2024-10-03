package com.ctecx.brsuite.chartofaccounts;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/account-chart-setup")
public class AccountChartFlowController {


    @GetMapping
    public String groupType() {

        return "accountcharts/All-Settings-AccountCharts";
    }

    @GetMapping("create-new")
    public String groupNames(Model model) {

        return "accountcharts/account-charts-records";
    }

    @GetMapping("records")
    public String accountChartsRecords(Model model) {

        return "accountcharts/account-charts-data";
    }


}
