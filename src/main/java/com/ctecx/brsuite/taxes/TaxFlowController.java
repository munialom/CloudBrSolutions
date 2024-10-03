package com.ctecx.brsuite.taxes;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/tax-data")
public class TaxFlowController {


    private final TaxClassRepository taxRepository;

    @GetMapping("/taxes")
    public String getTaxes(Model model) {
        model.addAttribute("taxes", taxRepository.findAll());
        return "rooms/taxes";
    }
}
