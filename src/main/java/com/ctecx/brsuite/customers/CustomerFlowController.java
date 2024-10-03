package com.ctecx.brsuite.customers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("customers")
@AllArgsConstructor
public class CustomerFlowController {




    @GetMapping
    public String productFlow(Model model) {

        return "customers/customer-details";
    }





}
