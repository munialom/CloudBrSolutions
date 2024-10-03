package com.ctecx.brsuite.revenue;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/revenue-codes")
public class RevenueFlowController {

    private final RevenueService revenueService;

    @GetMapping
    public String roomsControls(Model model) {

        return "revenue/revenue-management";
    }


    @GetMapping("form")
    public String Controls(Model model) {

        return "revenue/revenue-codes";
    }

    @GetMapping("/records")
    public String getRevenueCodes(Model model) {
        model.addAttribute("codes", revenueService.revenueList());
        return "revenue/codes-table";
    }




}
