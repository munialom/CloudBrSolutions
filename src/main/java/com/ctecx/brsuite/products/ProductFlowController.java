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

    @GetMapping("audit-trail")
    public String auditTrail() {

        return "products/my-audit-trail-data";
    }



    @GetMapping("current-list")
    public String updatedDataList() {

        return "products/my-products-list-data";
    }

    @GetMapping("current-prices")
    public String pricesData() {

        return "products/my-prices-data";
    }

    @GetMapping("current-stock-manager")
    public String stockManagerModule() {

        return "products/current-stock-manager";
    }

    @GetMapping("current-new-prices")
    public String priceManager() {

        return "products/current-price-manager";
    }



    @GetMapping("stock-levels")
    public String stockLevels() {

        return "products/stock-levels";
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


    @GetMapping("products-menu")
    public String productMenu() {

        return "stores/products-menu";
    }



    @GetMapping("purchaseOrders")
    public String purchaseOrders() {

        return "stores/purchase-orders-menu";
    }
    @GetMapping("stockIssuance-menu")
    public String stockIssuanceData() {

        return "stores/stock-issuance-menu";
    }
    @GetMapping("stockRequestsData")
    public String stockRequestsData() {

        return "stores/stock-request-menu";
    }


    @GetMapping("stock-transfers")
    public String stockTransfers() {

        return "stores/stock-transfer-menu";
    }


    @GetMapping("purchase-menu")
    public String purchaseMenu() {

        return "stores/purchase-menu";
    }





}
