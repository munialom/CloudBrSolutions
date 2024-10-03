package com.ctecx.brsuite.quotes;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quotes")
@AllArgsConstructor
public class QuotesController {



    @GetMapping
    public String productFlow(Model model) {

        return "quotes/Q-menu";
    }




    @GetMapping("workflow")
    public String Workflow(Model model) {

        return "quotes/quotes-management";
    }


    @GetMapping("invoices")
    public String InvoiceWorkflow(Model model) {

        return "invoices/invoices-management";
    }


    @GetMapping("new-invoice")
    public String newInvoice(Model model) {

        return "invoices/INV-menu";
    }


}
