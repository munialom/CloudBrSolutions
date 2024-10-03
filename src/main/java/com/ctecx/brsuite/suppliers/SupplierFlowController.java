package com.ctecx.brsuite.suppliers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("suppliers")
@AllArgsConstructor
public class SupplierFlowController {

    @GetMapping
    public String productFlow(Model model) {
        return "suppliers/suppliersControlPanel";
    }

    @GetMapping("list")
    public String supplierList(Model model) {
        return "suppliers/suppliers-list";
    }


    @GetMapping("form")
    public String transactions(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "suppliers/suppliers-add";
    }

    @GetMapping("receive-stocks")
    public String receivePurchases() {
        return "suppliers/receive-stocks";
    }

    @GetMapping("procurement")
    public String procurement() {
        return "suppliers/workflow-add";
    }

    // New endpoint for stock request
    @GetMapping("approved-orders")
    public String stockRequest(Model model) {
        // Add any necessary attributes to the model
        return "suppliers/approved-orders";
    }

    @GetMapping("cancelled-orders")
    public String cancelled(Model model) {
        // Add any necessary attributes to the model
        return "suppliers/cancelled-orders";
    }

    @GetMapping("completed-orders")
    public String complete(Model model) {
        // Add any necessary attributes to the model
        return "suppliers/complete-orders";
    }


    @GetMapping("purchase-order")
    public String purchaseOrder(Model model) {
        // Add any necessary attributes to the model
        return "suppliers/purchase-orders";
    }

    // New endpoint for stock issuance
    @GetMapping("supplier-managers")
    public String pendingOrders(Model model) {
        // Add any necessary attributes to the model
        return "suppliers/my-suppliers-manager";
    }
}
