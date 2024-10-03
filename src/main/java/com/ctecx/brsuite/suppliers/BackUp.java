package com.ctecx.brsuite.suppliers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("suppliers-bk")
@AllArgsConstructor
public class BackUp {

    @GetMapping
    public String productFlow(Model model) {

        return "suppliers/supplier-details";
    }

    @GetMapping("form")
    public String transactions(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "suppliers/supplier-form";
    }

    @GetMapping("controls")
    public String gariControlData(Model model) {

        return "controls/my-supplier::pc";
    }
}
