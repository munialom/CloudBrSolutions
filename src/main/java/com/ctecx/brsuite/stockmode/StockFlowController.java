package com.ctecx.brsuite.stockmode;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("stocks")
@AllArgsConstructor
public class StockFlowController {



    @GetMapping
    public String stockFlow(Model model) {

        return "stocks/opening-stock";
    }


    @GetMapping("management")
    public String stockManager(Model model) {

        return "stocks/stock-management";
    }



    @GetMapping("prices")
    public String priceControls(Model model) {

        return "stocks/prices-data";
    }


    @GetMapping("stock-records")
    public String priceSheet(Model model) {

        return "stocks/stock-records";
    }

    @GetMapping("stock-list")
    public String stockList(Model model) {

        return "stocks/stock-list";
    }


    @GetMapping("purchases")
    public String stockReceive(Model model) {

        return "purchase/receive-stocks";
    }


    @GetMapping("issuance")
    public String stockIssuance(Model model) {

        return "stocks/stock-issuance";
    }









}
