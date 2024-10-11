package com.ctecx.brsuite.products;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CustomProductService customProductService;



    @GetMapping("product-details")
    public String addingNewProduct() {

        return "products/products-details";
    }

    @GetMapping("pos-check")
    public String salesMenuDesign() {

        return "pos/my-pos-manager";
    }



   @GetMapping("product-controls")
    public String productManager() {

        return "products/my-products-manager";
    }



    @GetMapping("product-price-controls")
    public String productPriceManager() {

        return "products/my-price-manager";
    }

    @GetMapping("stock-records")
    public String allStockReports() {

        return "products/All-Stock-Reports";
    }

    @GetMapping("stock-reports-data")
    public String myStockReports() {

        return "products/stock-products-manager";
    }



    @GetMapping("adding-new-product")
    public String dashboard(Model model) {

        return "products/product-entry";

    }


    @GetMapping("product-workflow")
    public String workFlow(Model model) {

        return "products/products-management";
    }

    @GetMapping("product-view")
    public String workFlowView(Model model) {

        return "products/products-view";
    }


    @GetMapping("product-rx")
    public String workFlowRx(Model model) {

        return "products/product-transactions";
    }

    @GetMapping("upload")
    public String bulkUpload(Model model) {

        return "products/bulkUpload";
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public String createProduct(Product product) {
       productService.saveProduct(product);
        return"redirect:/";
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Optional<Product> existingProduct = productService.getProductById(id);
        if (existingProduct.isPresent()) {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/sales-data")
    public String searchProductsForSales(
            @RequestParam(value = "searchKey", required = false) String searchKey,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {

        if (searchKey != null && !searchKey.isEmpty()) {
            Page<ProductSaleDTO> productPage = customProductService.salesByProductNameAndCode(searchKey, page, size);
            model.addAttribute("products", productPage);
        }

        return "pos/search-table-pos";
    }





}
