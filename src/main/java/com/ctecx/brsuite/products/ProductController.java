package com.ctecx.brsuite.products;


import com.ctecx.brsuite.brands.BrandService;
import com.ctecx.brsuite.productcategory.CategoryService;
import com.ctecx.brsuite.taxes.TaxClassService;
import com.ctecx.brsuite.uom.UomService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final UomService uomService;
    private  final TaxClassService taxClassService;



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


    @GetMapping("/searchResults")
    public String searchProductsByName(@RequestParam(value = "productName", required = false) String productName, Model model) {
        if (productName != null && !productName.isEmpty()) {
            List<ProductInfoDTO> products = productService.calculateProductStock(productName);
            model.addAttribute("products", products);
        }
        return "stocks/search-table";
    }


    @GetMapping("/searchResults-prices")
    public String searchProductsByNameVersion2(@RequestParam(value = "productName", required = false) String productName, Model model) {
        if (productName != null && !productName.isEmpty()) {
            List<ProductInfoDTO> products = productService.calculateProductStock(productName);
            model.addAttribute("products", products);
        }
        return "stocks/search-table-prices";
    }

    @GetMapping("/searchResults-purchase")
    public String searchProductsForPurchase(@RequestParam(value = "productName", required = false) String productName, Model model) {
        if (productName != null && !productName.isEmpty()) {
            List<ProductInfoDTO> products = productService.calculateProductStock(productName);
            model.addAttribute("products", products);
        }
        return "purchase/search-table-purchase";
    }

    @GetMapping("/searchResults-pos")
    public String searchProductsForSale(@RequestParam(value = "productName", required = false) String productName, Model model) {
        if (productName != null && !productName.isEmpty()) {
            List<ProductSaleDTO> products = productService.salesMode(productName);
            model.addAttribute("products", products);
        }
        return "pos/search-table-pos";
    }




    @GetMapping("/sales")
    public String searchProductsForSales(@RequestParam(value = "searchKey", required = false) String searchKey, Model model, @PageableDefault(size = 5) Pageable pageable) {
        if (searchKey != null && !searchKey.isEmpty()) {
            List<ProductSaleDTO> products = productService.salesByProductNameAndCode(searchKey, pageable.getPageNumber(), pageable.getPageSize());
            model.addAttribute("products", products);
        }
        return "pos/search-table-pos";
    }



    @GetMapping("/all")
    public String getAllProducts(Model model,
                                 @RequestParam(required = false) String searchInput,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "15") int size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<ProductDataDTO> productDTOPage = productService.getAllProductDTOs(searchInput, pageable);

            if (searchInput != null) {
                model.addAttribute("search", searchInput);
            }

            model.addAttribute("products", productDTOPage.getContent());
            model.addAttribute("currentPage", productDTOPage.getNumber() + 1);
            model.addAttribute("totalItems", productDTOPage.getTotalElements());
            model.addAttribute("totalPages", productDTOPage.getTotalPages());
            model.addAttribute("pageSize", size);

        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "stocks/stock-list";
    }



    @GetMapping("list-pc")
    public String cleanControls(Model model) {

        return "controls/products-list::pc";
    }


}
