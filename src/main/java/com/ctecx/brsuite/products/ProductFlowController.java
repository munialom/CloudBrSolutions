package com.ctecx.brsuite.products;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/productsWorkFlow")
public class ProductFlowController {

    @GetMapping("controlPanel")
    public String addingNewProduct() {

        return "products/productsControlPanel";
    }


    @GetMapping("current-list")
    public String updatedDataList() {

        return "products/my-products-list-data";
    }

    @GetMapping("history-data")
    public String allHistoryDataList() {

        return "products/historical-data";
    }

    @GetMapping("brands")
    public String addBrand() {

        return "products/brands-entry";
    }

    @GetMapping("categories")
    public String addCategory() {

        return "products/categories-entry";
    }

    @GetMapping("list")
    public String productsList() {

        return "products/list";
    }

    @GetMapping("stocks")
    public String stocksData() {

        return "products/stocksControlPanel";
    }


    @GetMapping("stores")
    public String stores() {

        return "system/stores-add";
    }

}
