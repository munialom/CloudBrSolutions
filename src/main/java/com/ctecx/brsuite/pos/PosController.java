package com.ctecx.brsuite.pos;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pos")
@AllArgsConstructor
public class PosController {


    @GetMapping
    public String productFlow(Model model) {

        return "pos/pos-menu";
    }
    @GetMapping("cashier")
    public String cashMenu(Model model) {

        return "pos/billingControlPanel";
    }

    @GetMapping("bills")
    public String roomsMenu(Model model) {

        return "pos/pos-cashier";
    }


    @GetMapping("workflow")
    public String posWorkFlow(Model model) {

        return "pos/pos-management";
    }


    @GetMapping("room-sales")
    public String roomSalesWorkFlow(Model model) {

        return "pos/pos-rooms";
    }

  /*  @GetMapping("workflow")
    public String posWorkFlow(Model model) {

        return "pos/pos-control";
    }*/
}
